package com.rousetime.sample.startup.multiple

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.rousetime.android_startup.annotation.MultipleProcess
import com.venry.apt_annotation.AutoStartup

/**
 * Created by idisfkj on 2020/9/14.
 * Email: idisfkj@gmail.com.
 */
@AutoStartup(
    callCreateOnMainThread = false,
    waitOnMainThread = true,
    mouldName = arrayOf("MultipleTest"),
)
abstract class SampleMultipleThirdAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return SampleMultipleThirdAnnotateStartup::class.java.name
    }

}