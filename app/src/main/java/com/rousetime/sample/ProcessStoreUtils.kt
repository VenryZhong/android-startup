package com.rousetime.sample

import android.content.Context
import com.rousetime.android_startup.model.StartupSortStore
import com.rousetime.android_startup.utils.ProcessUtils
import com.venry.startup.StartupSortStoreConfig

object ProcessStoreUtils {
    fun getStoreConfig(context: Context): Pair<Int, StartupSortStore> {
        val processName = ProcessUtils.getProcessName(context)
        return if (processName == "${context.packageName}:multiple.process.service") {
            StartupSortStoreConfig.getMultipleServiceConfig()
        } else {
            StartupSortStoreConfig.getMultipleTestConfig()
        }
    }
}