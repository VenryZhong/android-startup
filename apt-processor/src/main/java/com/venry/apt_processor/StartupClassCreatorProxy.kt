package com.venry.apt_processor

import com.venry.apt_annotation.AutoStartup
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.venry.lib_base.extensions.getUniqueKey
import java.util.*
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class StartupClassCreatorProxy(
    val elementUtils: Elements,
    val element: TypeElement,
    val autoStartup: AutoStartup
) {
    private val startupClassName: String = startupClassName(element.simpleName.toString())

    fun generateCode(filer: Filer) {

        val file = FileSpec.builder(packageName, startupClassName)
            .addType(
                TypeSpec.classBuilder(startupClassName)
                    .superclass(ClassName("", element.qualifiedName.toString()))
                    .apply {
                        if (autoStartup.threadPriority != 0) {
                            addAnnotation(
                                AnnotationSpec.builder(
                                    ClassName(
                                        threadPriorityPackage,
                                        "ThreadPriority"
                                    )
                                )
                                    .addMember("priority = ${autoStartup.threadPriority}").build()
                            )
                        }
                    }
                    .addFunction(
                        FunSpec.builder("callCreateOnMainThread")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(Boolean::class.java)
                            .addStatement("return ${autoStartup.callCreateOnMainThread}")
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("waitOnMainThread")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(Boolean::class.java)
                            .addStatement("return ${autoStartup.waitOnMainThread}")
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("dependenciesByName")
                            .addModifiers(KModifier.OVERRIDE)
                            .returns(
                                ClassName("kotlin.collections", "List").parameterizedBy(
                                    ClassName("kotlin", "String")
                                ).copy(nullable = true)
                            )
                            .addStatement(
                                "return listOf(${
                                    autoStartup.dependenciesClassName.map { "\"${it}\"" }
                                        .joinToString(
                                            ","
                                        )
                                })"
                            )
                            .build()
                    )
                    .build()
            )
            .build()

        file.writeTo(filer)
    }

    data class MapInfo(
        val mouldName: String,
        val result: List<String>,
        val startupMap: HashMap<String, StartupClassCreatorProxy>,
        val startupChildrenMap: HashMap<String, MutableList<String>>,
        val needAwaitCount: Int
    )

    companion object {

        private const val packageName = "com.venry.startup"
        private const val startupPackage = "com.rousetime.android_startup"
        private const val modelPackage = "com.rousetime.android_startup.model"
        private const val threadPriorityPackage = "com.rousetime.android_startup.annotation"

        fun startupClassName(name: String): String {
            return name + "_AUTO"
        }

        fun generateModuleCode(
            mouldMap: HashMap<String, ArrayList<StartupClassCreatorProxy>>,
            filer: Filer,
            messager: Messager
        ) {

            val list = ArrayList<MapInfo>()
            for (key in mouldMap.keys) {
                val proxyInfoList = mouldMap[key]!!

                val mainResult = mutableListOf<String>()
                val mainStartupProxyResult = mutableListOf<StartupClassCreatorProxy>()
                val ioResult = mutableListOf<String>()
                val ioStartupProxyResult = mutableListOf<StartupClassCreatorProxy>()
                val temp = mutableListOf<String>()
                val startupMap = hashMapOf<String, StartupClassCreatorProxy>()
                val zeroDeque = ArrayDeque<String>()
                val startupChildrenMap = hashMapOf<String, MutableList<String>>()
                val inDegreeMap = hashMapOf<String, Int>()
                var needAwaitCount = 0
                proxyInfoList.forEach {
                    val autoStartup = it.autoStartup
                    val className = startupClassName(it.element.simpleName.toString())
                    val uniqueKey = ("$packageName.$className").getUniqueKey()
                    if (it.autoStartup.waitOnMainThread && !it.autoStartup.callCreateOnMainThread) {
                        needAwaitCount++
                    }
                    if (!startupMap.containsKey(uniqueKey)) {
                        startupMap[uniqueKey] = it
                        // save in-degree
                        inDegreeMap[uniqueKey] = autoStartup.dependenciesClassName.size
                        if (autoStartup.dependenciesClassName.isEmpty()) {
                            zeroDeque.offer(uniqueKey)
                        } else {
                            // add key parent, value list children
                            autoStartup.dependenciesClassName.forEach { parent ->
                                val lastIndex = parent.indexOfLast { it == '.' }
                                val parentUniqueKey = startupClassName(
                                    "$packageName." + if (lastIndex >= 0) {
                                        parent.substring(lastIndex + 1, parent.length)
                                    } else {
                                        parent
                                    }
                                ).getUniqueKey()
                                if (startupChildrenMap[parentUniqueKey] == null) {
                                    startupChildrenMap[parentUniqueKey] = arrayListOf()
                                }
                                startupChildrenMap[parentUniqueKey]?.add(uniqueKey)
                            }
                        }
                    } else {
                        messager.printMessage(Diagnostic.Kind.ERROR, "$it multiple add.")
                    }
                }

                while (!zeroDeque.isEmpty()) {
                    zeroDeque.poll()?.let {
                        startupMap[it]?.let { startupClassCreatorProxy ->
                            val className =
                                startupClassName(startupClassCreatorProxy.element.simpleName.toString())
                            temp.add(className)
                            // add zero in-degree to result list
                            if (startupClassCreatorProxy.autoStartup.callCreateOnMainThread) {
                                mainResult.add(className)
                                mainStartupProxyResult.add(startupClassCreatorProxy)
                            } else {
                                ioResult.add(className)
                                ioStartupProxyResult.add(startupClassCreatorProxy)
                            }
                        }
                        startupChildrenMap[it]?.forEach { children ->
                            inDegreeMap[children] = inDegreeMap[children]?.minus(1) ?: 0
                            // add zero in-degree to deque
                            if (inDegreeMap[children] == 0) {
                                zeroDeque.offer(children)
                            }
                        }
                    }
                }

                if (mainResult.count() + ioResult.count() != proxyInfoList.count()) {
                    messager.printMessage(
                        Diagnostic.Kind.ERROR,
                        "lack of dependencies or have circle dependencies. ${mainResult} ${ioResult} ${proxyInfoList.count()}"
                    )
                }

                val result = mutableListOf<String>().apply {
                    addAll(ioResult)
                    addAll(mainResult)
                }
                printLogInfo(arrayListOf<StartupClassCreatorProxy>().apply {
                    addAll(ioStartupProxyResult)
                    addAll(mainStartupProxyResult)
                }, messager)

                list.add(MapInfo(key, result, startupMap, startupChildrenMap, needAwaitCount))
            }

            if (list.isEmpty()) return

            // print code
            val file = FileSpec.builder(
                packageName,
                "StartupSortStoreConfig"
            ).addType(
                TypeSpec.objectBuilder("StartupSortStoreConfig")
                    .apply {
                        list.forEach { mapInfo ->
                            val returnSecondClass = ClassName(modelPackage, "StartupSortStore")
                            addFunction(
                                FunSpec.builder("get${if (mapInfo.mouldName.isBlank()) "Default" else mapInfo.mouldName}Config")
                                    .returns(
                                        Pair::class.asClassName().parameterizedBy(
                                            Int::class.asClassName(),
                                            returnSecondClass
                                        )
                                    )
                                    .apply {
                                        val all = TypeVariableName("*")
                                        val arrayList = ClassName("kotlin.collections", "ArrayList")
                                        val superClass = ClassName(startupPackage, "Startup")
                                        superClass.parameterizedBy(all)
                                        addStatement(
                                            "val result = %T()",
                                            arrayList.parameterizedBy(superClass.parameterizedBy(all))
                                        )
                                        val startupMapClass = HashMap::class.asClassName()
                                            .parameterizedBy(
                                                ClassName("kotlin", "String"),
                                                superClass.parameterizedBy(all)
                                            )
                                        addStatement("val startupMap=%T()", startupMapClass)


                                        val startupChildrenMapClass = HashMap::class.asClassName()
                                            .parameterizedBy(
                                                ClassName("kotlin", "String"),
                                                arrayList.parameterizedBy(
                                                    ClassName(
                                                        "kotlin",
                                                        "String"
                                                    )
                                                )
                                            )
                                        addStatement(
                                            "val startupChildrenMap=%T()",
                                            startupChildrenMapClass
                                        )

                                        val resultList = mapInfo.result
                                        var i = 0
                                        for (className in resultList) {
                                            val newStartup = "newStartup${i}"
                                            addStatement(
                                                "val ${newStartup}=%T()",
                                                ClassName(packageName, className)
                                            )
                                            addStatement("result.add(${newStartup})")
                                            val uniqueKey =
                                                ("$packageName.$className").getUniqueKey()
                                            addStatement("startupMap[\"${uniqueKey}\"]=${newStartup}")
                                            i++
                                        }
                                        for (key in mapInfo.startupChildrenMap.keys) {
                                            addStatement(
                                                "startupChildrenMap[\"${key}\"]=arrayListOf(${
                                                    mapInfo.startupChildrenMap[key]!!.map { "\"${it}\"" }
                                                        .joinToString(",")
                                                })"
                                            )
                                        }
                                    }
                                    .addStatement("return Pair(${mapInfo.needAwaitCount} , StartupSortStore(result,startupMap,startupChildrenMap))")
                                    .build()
                            )
                        }
                    }
                    .build()
            )
                .build()

            file.writeTo(filer)

        }

        private fun printLogInfo(result: List<StartupClassCreatorProxy>, messager: Messager) {
            val printBuilder = buildString {
                append("\n")
                append("|================================================================")
                result.forEachIndexed { index, it ->
                    append("\n")
                    append("|         order          |    [${index + 1}] ")
                    append("\n")
                    append("|----------------------------------------------------------------")
                    append("\n")
                    append("|        Startup         |    ${it.element.simpleName}")
                    append("\n")
                    append("|----------------------------------------------------------------")
                    append("\n")
                    append("|   Dependencies size    |    ${it.autoStartup.dependenciesClassName.size}")
                    append("\n")
                    append("|----------------------------------------------------------------")
                    append("\n")
                    append("| callCreateOnMainThread |    ${it.autoStartup.callCreateOnMainThread}")
                    append("\n")
                    append("|----------------------------------------------------------------")
                    append("\n")
                    append("|    waitOnMainThread    |    ${it.autoStartup.waitOnMainThread}")
                    append("\n")
                    append("|================================================================")
                }
            }
            messager.printMessage(Diagnostic.Kind.NOTE, printBuilder)
        }
    }


}