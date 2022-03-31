package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = true,
    waitOnMainThread = false,
    mouldName = arrayOf("ManualDispatch")
)
abstract class SampleManualDispatchAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        Thread {
            Thread.sleep(2000)
            // manual dispatch
            onDispatch()
        }.start()
        return "manual dispatch"
    }


    override fun manualDispatch(): Boolean = true

}