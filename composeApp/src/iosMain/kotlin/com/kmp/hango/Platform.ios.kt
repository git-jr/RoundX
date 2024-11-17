package com.kmp.hango

import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIDevice
import platform.UIKit.*

import platform.CoreGraphics.*
import kotlinx.cinterop.*

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

@OptIn(ExperimentalForeignApi::class)
actual fun takeScreenshot() {

    // Inicia o contexto gráfico

    val window = UIApplication.sharedApplication.keyWindow ?: return

    val size = memScoped {
        // Obtém um ponteiro para window.bounds
        val boundsPtr = window.bounds.getPointer(this)
        val bounds = boundsPtr.pointed

        // Acessa os campos width e height de CGSize
        val width = bounds.size.width
        val height = bounds.size.height

        // Cria um CGSize com os valores obtidos
        CGSizeMake(width, height)
    }

    UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
    window.drawViewHierarchyInRect(window.bounds, afterScreenUpdates = false)
    val image = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    // Salva a imagem no álbum de fotos
    if (image != null) {
        UIImageWriteToSavedPhotosAlbum(image, null, null, null)
        println("12345Tag: Screenshot salvo no álbum de fotos")
    } else {
        println("12345Tag: Falha ao capturar o screenshot")
    }
}