package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(callCreateOnMainThread = false, waitOnMainThread = false ,
    mouldName = arrayOf("AsyncAndAsync"))
abstract class SampleAsyncFiveAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        Thread.sleep(1000)
        return "async five"
    }

}