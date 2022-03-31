package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    dependenciesClassName = ["com.rousetime.sample.startup_annotate.SampleFirstAnnotateStartup",
        "com.rousetime.sample.startup_annotate.SampleSecondAnnotateStartup",
        "com.rousetime.sample.startup_annotate.SampleThirdAnnotateStartup"]
)
abstract class SampleFourthAnnotateStartup : AndroidStartup<Any>() {

    override fun create(context: Context): Any? {
        Thread.sleep(100)
        return null
    }

}