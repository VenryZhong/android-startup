package com.rousetime.sample.startup_annotate

import android.content.Context
import android.util.Log
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.rousetime.android_startup.executor.ExecutorManager
import com.venry.apt_annotation.AutoStartup
import java.util.concurrent.Executor


@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = true,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleFirstAnnotateStartup"]
)
abstract class SampleSecondAnnotateStartup : AndroidStartup<Boolean>() {

    override fun create(context: Context): Boolean {
        Thread.sleep(5000)
        return true
    }

    override fun createExecutor(): Executor {
        return ExecutorManager.instance.cpuExecutor
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        Log.d(
            "SampleSecondStartup",
            "onDependenciesCompleted: ${startup::class.java.simpleName}, $result"
        )
    }
}