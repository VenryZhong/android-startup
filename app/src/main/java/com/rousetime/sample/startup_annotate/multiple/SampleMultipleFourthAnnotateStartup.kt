package com.rousetime.sample.startup.multiple

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.annotation.MultipleProcess
import com.venry.apt_annotation.AutoStartup

@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = false,
    mouldName = arrayOf("MultipleService" , "MultipleTest"),
)
abstract class SampleMultipleFourthAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        Thread.sleep(1000)
        return SampleMultipleFourthAnnotateStartup::class.java.simpleName
    }

}