package com.kmp.hango

import android.app.Application
import com.kmp.hango.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@Application)
        }
    }
}