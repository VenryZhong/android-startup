package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = true,
    waitOnMainThread = false,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleAsyncTwoAnnotateStartup"],
    mouldName = arrayOf("AsyncAndSync")
)
abstract class SampleSyncFourAnnotateStartup: AndroidStartup<String>() {

    private var mResult: String? = null

    override fun create(context: Context): String? {
        return "$mResult + sync four"
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        mResult = result as? String?
    }
}