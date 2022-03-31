package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(callCreateOnMainThread = false , waitOnMainThread = true , dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleAsyncSixAnnotateStartup"], mouldName = arrayOf("AsyncAndAsyncAwaitMainThread"))
abstract class SampleAsyncFourAnnotateStartup : AndroidStartup<String>() {

    private var mResult: String? = null

    override fun create(context: Context): String? {
        Thread.sleep(1000)
        return "$mResult + async four"
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        mResult = result as? String?
    }
}