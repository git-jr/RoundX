package com.kmp.hango

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import dev.gitlive.firebase.storage.File
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIDevice
import platform.UIKit.*
import platform.CoreGraphics.*
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGBitmapContextCreate
import platform.UIKit.UIImage
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned


class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

private fun presentShareSheet(image: UIImage) {
    // Cria um array com os itens a serem compartilhados
    val itemsToShare = listOf(image)

    // Cria o UIActivityViewController com os itens
    val activityViewController = UIActivityViewController(itemsToShare, null)

    // Obtém o UIViewController atual para apresentar o activityViewController
    val currentViewController =
        UIApplication.sharedApplication.keyWindow?.rootViewController ?: return

    // Apresenta o activityViewController no thread principal
    currentViewController.presentViewController(
        activityViewController,
        animated = true,
        completion = null
    )
}


@OptIn(ExperimentalForeignApi::class)
fun ImageBitmap.toUIImage(): UIImage {
    // Obtém o mapa de pixels da ImageBitmap
    val pixelMap = this.toPixelMap()
    val width = this.width
    val height = this.height

    // Cria um array de bytes para armazenar os dados de RGBA
    val byteArray = ByteArray(width * height * 4) // 4 bytes por pixel (RGBA)

    // Popula o array de bytes com os dados de cores
    var index = 0
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = pixelMap[x, y]
            byteArray[index++] = (color.red * 255).toInt().toByte()   // Red
            byteArray[index++] = (color.green * 255).toInt().toByte() // Green
            byteArray[index++] = (color.blue * 255).toInt().toByte()  // Blue
            byteArray[index++] = (color.alpha * 255).toInt().toByte() // Alpha
        }
    }

    // Converte o byteArray em um UIImage
    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = byteArray.usePinned { pinnedArray ->
        CGBitmapContextCreate(
            data = pinnedArray.addressOf(0),
            width = width.toULong(),
            height = height.toULong(),
            bitsPerComponent = 8u,
            bytesPerRow = (width * 4).toULong(), // 4 bytes por pixel
            space = colorSpace,
            bitmapInfo = (CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value)
        )
    }

    val cgImage = CGBitmapContextCreateImage(context) ?: return UIImage()
    return UIImage.imageWithCGImage(cgImage)
}

actual class ScreenshotManager {
    actual fun shareImage(image: ImageBitmap) {
        // Converte a ImageBitmap em UIImage
        val uiImage = image.toUIImage()

        // Apresenta a modal de compartilhamento com a imagem
        presentShareSheet(uiImage)
    }
}

actual fun PlatformFile.toFirebaseFile(): File {
    return File(this.nsUrl)
}