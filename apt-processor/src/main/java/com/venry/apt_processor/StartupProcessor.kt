package com.venry.lib

import com.venry.apt_annotation.AutoStartup
import com.venry.apt_processor.StartupClassCreatorProxy
import com.google.auto.service.AutoService
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic


@AutoService(Processor::class)
class StartupProcessor : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements
    private val proxyMap: HashMap<String, StartupClassCreatorProxy> = HashMap()
    private val mouldMap: HashMap<String, ArrayList<StartupClassCreatorProxy>> = HashMap()

    //    init：初始化。可以得到ProcessingEnviroment，ProcessingEnviroment提供很多有用的工具类Elements, Types 和 Filer
    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
    }

    //    getSupportedAnnotationTypes：指定这个注解处理器是注册给哪个注解的，这里说明是注解BindView
    override fun getSupportedAnnotationTypes(): Set<String>? {
        val supportTypes: HashSet<String> = LinkedHashSet()
        supportTypes.add(AutoStartup::class.java.canonicalName)
        return supportTypes
    }


    //    getSupportedSourceVersion：指定使用的Java版本，通常这里返回SourceVersion.latestSupported()
    override fun getSupportedSourceVersion(): SourceVersion? {
        return SourceVersion.latestSupported()
    }

    //    process：可以在这里写扫描、评估和处理注解的代码，生成Java文件（process中的代码下面详细说明）
    override fun process(
        p0: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        messager.printMessage(Diagnostic.Kind.NOTE, "processing...")
        proxyMap.clear()
        mouldMap.clear()
        val elements: Set<Element?> =
            roundEnvironment?.getElementsAnnotatedWith(AutoStartup::class.java) ?: return true
        for (element in elements) {
            if (element is TypeElement) {
                val fullClassName = element.qualifiedName.toString()
                var proxy: StartupClassCreatorProxy? = proxyMap[fullClassName]
                val annotation = element.getAnnotation(AutoStartup::class.java)
                if (proxy == null) {
                    proxy = StartupClassCreatorProxy(
                        elementUtils,
                        element,
                        annotation
                    )
                    proxyMap[fullClassName] = proxy
                }

                fun addNameToMap(name: String) {
                    var get = mouldMap[name]
                    if (get == null) {
                        val newList = ArrayList<StartupClassCreatorProxy>()
                        get = newList
                        mouldMap[name] = get
                    }
                    get!!.add(proxy)
                }

                addNameToMap("")
                annotation.mouldName.forEach {
                    addNameToMap(it)
                }
            }
        }
        for (key in proxyMap.keys) {
            val proxyInfo = proxyMap[key]!!
            try {
                proxyInfo.generateCode(processingEnv.filer)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        StartupClassCreatorProxy.generateModuleCode(mouldMap, processingEnv.filer , messager)

        messager.printMessage(Diagnostic.Kind.NOTE, "process finish ...")
        return true
    }

}