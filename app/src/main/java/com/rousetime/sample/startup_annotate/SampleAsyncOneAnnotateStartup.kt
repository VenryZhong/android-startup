package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleSyncThreeAnnotateStartup"],
    mouldName = arrayOf("SyncAndAsync")
)
abstract class SampleAsyncOneAnnotateStartup : AndroidStartup<String>() {

    private var mResult: String? = null

    override fun create(context: Context): String? {
        Thread.sleep(2000)
        return "$mResult + async one"
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        mResult = result as? String?
    }
}