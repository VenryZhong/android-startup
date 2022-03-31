package com.rousetime.sample.startup.priority

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.venry.apt_annotation.AutoStartup

@AutoStartup(callCreateOnMainThread = false , waitOnMainThread = false ,    mouldName = arrayOf("threadPriority"))
abstract class SamplePrioritySecondAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        val i = buildString {
            repeat(1000000) {
                append("$it")
            }
        }
        return SamplePrioritySecondAnnotateStartup::class.java.simpleName
    }

}