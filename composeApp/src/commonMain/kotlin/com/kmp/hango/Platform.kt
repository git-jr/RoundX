package com.kmp.hango

import androidx.compose.ui.graphics.ImageBitmap

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class ScreenshotManager {
    fun shareImage(image: ImageBitmap)
}