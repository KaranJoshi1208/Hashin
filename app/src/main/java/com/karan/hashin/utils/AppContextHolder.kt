package com.karan.hashin.utils

import android.content.Context

/** Simple holder to provide appContext where DI is not set up. */
object AppContextHolder {
    lateinit var appContext: Context
}

