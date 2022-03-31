package com.rousetime.sample.startup_annotate

import android.content.Context
import com.rousetime.android_startup.AndroidStartup
import com.venry.apt_annotation.AutoStartup


@AutoStartup(
    callCreateOnMainThread = true,
    waitOnMainThread = false,
    mouldName = arrayOf("SyncAndAsync")
)
abstract class SampleSyncThreeAnnotateStartup : AndroidStartup<String>() {

    override fun create(context: Context): String? {
        return "sync three"
    }

}