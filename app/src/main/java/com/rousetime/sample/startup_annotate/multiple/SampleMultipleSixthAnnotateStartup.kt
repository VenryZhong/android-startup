package com.rousetime.sample.startup.multiple

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.rousetime.android_startup.annotation.MultipleProcess
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = true,
    mouldName = arrayOf("MultipleService"),
    dependenciesClassName = arrayOf("com.rousetime.sample.startup.multiple.SampleMultipleFifthAnnotateStartup")
)
abstract class SampleMultipleSixthAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return SampleMultipleSixthAnnotateStartup::class.java.simpleName
    }

}