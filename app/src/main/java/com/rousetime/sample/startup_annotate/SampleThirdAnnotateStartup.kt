package com.rousetime.sample.startup_annotate

import android.content.Context
import android.util.Log
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleFirstAnnotateStartup",
        "com.rousetime.sample.startup_annotate.SampleSecondAnnotateStartup"]
)
abstract class SampleThirdAnnotateStartup : AndroidStartup<Long>() {

    override fun create(context: Context): Long? {
        Thread.sleep(3000)
        return 10L
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        Log.d(
            "SampleThirdStartup",
            "onDependenciesCompleted: ${startup::class.java.simpleName}, $result"
        )
    }
}