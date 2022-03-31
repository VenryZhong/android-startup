package com.rousetime.sample.startup.multiple

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.Startup
import com.rousetime.android_startup.annotation.MultipleProcess
import com.venry.apt_annotation.AutoStartup


@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    dependenciesClassName = arrayOf("com.rousetime.sample.startup.multiple.SampleMultipleFirstAnnotateStartup")
)
@MultipleProcess(":multiple.provider")
abstract class SampleMultipleSecondAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return SampleMultipleSecondAnnotateStartup::class.java.simpleName
    }

}