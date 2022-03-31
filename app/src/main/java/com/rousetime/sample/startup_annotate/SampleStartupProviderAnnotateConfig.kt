package com.rousetime.sample.startup_annotate

import android.util.Log
import com.rousetime.android_startup.StartupListener
import com.rousetime.android_startup.model.CostTimesModel
import com.rousetime.android_startup.model.LoggerLevel
import com.rousetime.android_startup.model.StartupConfig
import com.rousetime.android_startup.provider.StartupProviderConfig
import com.rousetime.sample.SampleApplication

/**
 * Created by idisfkj on 2020/7/28.
 * Email: idisfkj@gmail.com.
 */
class SampleStartupProviderAnnotateConfig : StartupProviderConfig {

    override fun getConfig(): StartupConfig =
        StartupConfig.Builder()
            .setLoggerLevel(LoggerLevel.DEBUG) // default LoggerLevel.NONE
            .setAwaitTimeout(12000L) // default 10000L
            .setOpenStatistics(true) // default true
            .setListener(object : StartupListener {
                override fun onCompleted(totalMainThreadCostTime: Long, costTimesModels: List<CostTimesModel>) {
                    // can to do cost time statistics.
                    SampleApplication.costTimesLiveData.value = costTimesModels
                    Log.d("StartupTrack", "onCompleted: ${costTimesModels.size}")
                }
            })
            .build()
}