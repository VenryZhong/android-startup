package com.rousetime.sample.startup.multiple

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.annotation.MultipleProcess
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
)
@MultipleProcess(":multiple.provider")
abstract class SampleMultipleFirstAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return SampleMultipleFirstAnnotateStartup::class.java.simpleName
    }

}