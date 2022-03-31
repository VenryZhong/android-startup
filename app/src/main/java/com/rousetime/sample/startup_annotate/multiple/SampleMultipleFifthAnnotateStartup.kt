package com.rousetime.sample.startup.multiple

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.annotation.MultipleProcess
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    mouldName = arrayOf("MultipleService"),
    dependenciesClassName = arrayOf("com.rousetime.sample.startup.multiple.SampleMultipleFourthAnnotateStartup")
)
abstract class SampleMultipleFifthAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return SampleMultipleFifthAnnotateStartup::class.java.simpleName
    }

}