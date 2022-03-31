Fork from https://github.com/idisfkj/android-startup

增加功能使用注解生成 StartUp ， 去除运行期 拓扑排序 的计算

使用 APT 生成 StartupSortStoreConfig 
之前是在运行期把 任务顺序 计算出来 , 现在在编译期把任务计算出来

使用： 

```kotlin

@AutoStartup(
    callCreateOnMainThread = false, // 是否允许在主线程
    waitOnMainThread = false, // 是否阻塞主线程
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleFirstAnnotateStartup",
        "com.rousetime.sample.startup_annotate.SampleSecondAnnotateStartup",
        "com.rousetime.sample.startup_annotate.SampleThirdAnnotateStartup"], // 依赖的任务
    mouldName = [""], // Startup 所属模块
    threadPriority  = 0  // 线程优先级
)
abstract class XXXXX : AndroidStartup<String>() { 
    // 抽象类， 无需实现 callCreateOnMainThread，waitOnMainThread，dependenciesClassName 方法
    // 最后由编译器按照注解生成
    
}

// 使用 StartupSortStoreConfig 来获取 store 的 配置
val storePair = StartupSortStoreConfig.getXXXXConfig()

StartupManager.Builder()
    .setConfig(config)
    .setStartupSortStore(storePair.second , storePair.first)
    .build(this)
    .start()
    .await()

详情可以参照 SampleCommonAnnotateActivity
        
目前对 Provider 尚未完好的支持
其他用法和原先的大同小异

mouldName的是意思是多种配置类型
如 StartupA , StartupB , StartupC , StartupD , StartupE
StartupA -> moduleName("A" , "B")
StartupB -> moduleName("A" )
StartupC -> moduleName("A" )
StartupD -> moduleName("A" , "B")
StartupE -> moduleName("A" , "B")

那么可以 StartupSortStoreConfig.getAConfig 最后的任务包含 StartupA，StartupB，StartupC，StartupD，StartupE
那么可以 StartupSortStoreConfig.getBConfig 最后的任务包含 StartupA，StartupD，StartupE
        
多进程也可以使用该办法来获取最后需要执行的任务
例如
object ProcessStoreUtils {
    fun getStoreConfig(context: Context): Pair<Int, StartupSortStore> {
        val processName = ProcessUtils.getProcessName(context)
        return if (processName == "${context.packageName}:multiple.process.service") {
            StartupSortStoreConfig.getMultipleServiceConfig()
        } else {
            StartupSortStoreConfig.getMultipleTestConfig()
        }
    }
}

```