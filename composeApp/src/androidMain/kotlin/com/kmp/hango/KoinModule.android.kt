package com.kmp.hango

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val targetModule: Module = module {
    singleOf(::ScreenshotManager)
    // Or
//    single { ScreenshotManager(get()) }
}