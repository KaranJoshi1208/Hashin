package com.karan.hashin

import android.app.Application
import com.karan.hashin.utils.AppContextHolder

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.appContext = applicationContext
    }
}

