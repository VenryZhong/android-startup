package com.rousetime.android_startup.extensions

import com.venry.lib_base.extensions.DEFAULT_KEY
import com.rousetime.android_startup.Startup

/**
 * Created by idisfkj on 2020/8/10.
 * Email: idisfkj@gmail.com.
 */
internal fun Class<out Startup<*>>.getUniqueKey(): String {
    return "$DEFAULT_KEY:$name"
}