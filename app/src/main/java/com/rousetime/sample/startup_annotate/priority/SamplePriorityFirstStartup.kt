package com.rousetime.sample.startup.priority

import android.content.Context
import android.os.Process
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.annotation.ThreadPriority
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    threadPriority = Process.THREAD_PRIORITY_URGENT_AUDIO,
    mouldName = arrayOf("threadPriority")
)
abstract class SamplePriorityFirstAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        val i = buildString {
            repeat(1000000) {
                append("$it")
            }
        }
        return SamplePriorityFirstAnnotateStartup::class.java.simpleName
    }

}