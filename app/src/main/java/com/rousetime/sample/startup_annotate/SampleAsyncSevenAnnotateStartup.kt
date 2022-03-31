package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleManualDispatchAnnotateStartup"],
    mouldName = arrayOf("ManualDispatch")
)
abstract class SampleAsyncSevenAnnotateStartup : AndroidStartup<String>() {

    private var mResult: String? = null

    override fun create(context: Context): String? {
        Thread.sleep(3000)
        return "$mResult + async seven"
    }

    override fun onDependenciesCompleted(startup: Startup<*>, result: Any?) {
        mResult = result as? String?
    }
}