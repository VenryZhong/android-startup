package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = true,
    waitOnMainThread = false,
)
abstract class SampleFirstAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return this.javaClass.simpleName
    }

}