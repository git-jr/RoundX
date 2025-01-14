package com.kmp.hango

import androidx.compose.ui.graphics.ImageBitmap
import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.core.PlatformFile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect class ScreenshotManager {
    fun shareImage(image: ImageBitmap)
}

expect fun PlatformFile.toFirebaseFile(): File