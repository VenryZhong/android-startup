package com.venry.apt_annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class AutoStartup(
    val waitOnMainThread: Boolean = true,
    val callCreateOnMainThread: Boolean = true,
    val dependenciesClassName: Array<String> = emptyArray(),
    val mouldName: Array<String> = emptyArray(),
    val threadPriority  :Int = 0
)
