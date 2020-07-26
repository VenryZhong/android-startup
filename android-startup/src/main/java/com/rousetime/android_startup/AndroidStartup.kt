package com.rousetime.android_startup

import com.rousetime.android_startup.executor.ExecutorManager
import com.rousetime.android_startup.executor.StartupExecutor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

/**
 * Created by idisfkj on 2020/7/23.
 * Email: idisfkj@gmail.com.
 */
abstract class AndroidStartup<T> : Startup<T>, StartupExecutor {

    private val waitCountDown by lazy { CountDownLatch(dependencies()?.size ?: 0) }

    fun toWait() {
        try {
            waitCountDown.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun toNotify() {
        waitCountDown.countDown()
    }

    override fun createExecutor(): Executor = ExecutorManager.instance.ioExecutor

    override fun dependencies(): List<Class<out Startup<*>>>? {
        return null
    }

    abstract fun isOnMainThread(): Boolean

    abstract fun isNeedWait(): Boolean

}