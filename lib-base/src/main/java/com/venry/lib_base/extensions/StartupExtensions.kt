package com.venry.lib_base.extensions
/**
 * Created by idisfkj on 2020/8/10.
 * Email: idisfkj@gmail.com.
 */
const val DEFAULT_KEY = "com.rousetime.android_startup.defaultKey"

fun String.getUniqueKey(): String = "$DEFAULT_KEY:$this"