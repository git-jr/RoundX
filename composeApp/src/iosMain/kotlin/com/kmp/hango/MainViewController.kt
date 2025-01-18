package com.kmp.hango

import androidx.compose.ui.window.ComposeUIViewController
import com.kmp.hango.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }