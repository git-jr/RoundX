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
actual fun takeScreenshot(context: Any?) {

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
// Codigo que salva na memoria interna
//        UIImageWriteToSavedPhotosAlbum(image, null, null, null)
//        println("12345Tag: Screenshot salvo no álbum de fotos")


        // Apresenta a modal de compartilhamento com a imagem
        presentShareSheet(image)
        println("12345Tag: Screenshot compartilhado no álbum de fotos")

    } else {
        println("12345Tag: Falha ao capturar o screenshot")
    }
}


private fun presentShareSheet(image: UIImage) {
    // Cria um array com os itens a serem compartilhados
    val itemsToShare = listOf(image)

    // Cria o UIActivityViewController com os itens
    val activityViewController = UIActivityViewController(itemsToShare, null)

    // Obtém o UIViewController atual para apresentar o activityViewController
    val currentViewController = UIApplication.sharedApplication.keyWindow?.rootViewController ?: return

    // Apresenta o activityViewController no thread principal
    currentViewController.presentViewController(activityViewController, animated = true, completion = null)
}