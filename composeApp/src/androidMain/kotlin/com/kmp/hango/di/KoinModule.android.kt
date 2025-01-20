package com.kmp.hango.di
import com.kmp.hango.ScreenshotManager
import com.kmp.hango.data.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module = module {
    single { ScreenshotManager(get()) }
    single { createDataStore(androidContext()) }
}