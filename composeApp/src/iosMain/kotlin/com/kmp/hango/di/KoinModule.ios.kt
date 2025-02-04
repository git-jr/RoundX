package com.kmp.hango.di

import com.kmp.hango.ScreenshotManager
import com.kmp.hango.data.createDataStore
import com.kmp.hango.data.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val targetModule: Module = module {
    single { ScreenshotManager() }
    single { createDataStore() }
    single { getDatabaseBuilder()}
}