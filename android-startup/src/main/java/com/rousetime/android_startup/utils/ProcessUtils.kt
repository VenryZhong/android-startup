package com.rousetime.android_startup.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * Created by idisfkj on 2020/9/14.
 * Email: idisfkj@gmail.com.
 */
object ProcessUtils {

    fun getProcessName(context: Context): String {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val myPid = Process.myPid()
        am.runningAppProcesses.forEach {
            if (it.pid == myPid) {
                return it.processName
            }
        }
        return ""
    }

    fun isMainProcess(context: Context): Boolean = getProcessName(context) == context.packageName

    fun isMultipleProcess(context: Context, processName: Array<out String>): Boolean {
        processName.forEach {
            if (getProcessName(context) == "${context.packageName}$it") {
                return true
            }
        }
        return false
    }
}