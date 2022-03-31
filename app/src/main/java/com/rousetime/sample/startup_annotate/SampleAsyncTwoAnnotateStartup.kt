package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    mouldName = arrayOf("AsyncAndSync")
)
abstract class SampleAsyncTwoAnnotateStartup: AndroidStartup<String>() {

    override fun create(context: Context): String? {
        Thread.sleep(3000)
        return "async two"
    }

}