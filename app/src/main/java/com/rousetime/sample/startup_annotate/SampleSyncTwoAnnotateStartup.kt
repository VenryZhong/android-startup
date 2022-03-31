package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = true,
    waitOnMainThread = false,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleSyncOneAnnotateStartup"],
    mouldName = arrayOf("SyncAndSync")
)
abstract class SampleSyncTwoAnnotateStartup: AndroidStartup<String>() {

    private var mResult: String? = null

    override fun create(context: Context): String? {
        return "$mResult + sync two"
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        mResult = result as? String?
    }

}