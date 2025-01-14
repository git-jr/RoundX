package com.kmp.hango

import android.os.Build
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import io.github.vinceglb.filekit.core.PlatformFile

import dev.gitlive.firebase.storage.File as FirebaseFile

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

private fun saveBitmapToCache(bitmap: Bitmap, activity: Activity): File? {
    return try {
        val cachePath = File(activity.cacheDir, "images")
        cachePath.mkdirs()
        val fileName = "screenshot.png"
        val file = File(cachePath, fileName)
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        file
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private fun shareImage(imageFile: File, activity: Context) {
    // Obtém o URI do arquivo usando FileProvider
    val imageUri: Uri = FileProvider.getUriForFile(
        activity,
        "${activity.packageName}.fileprovider",
        imageFile
    )


    // Cria a Intent de compartilhamento
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    // Inicia a Activity de compartilhamento
    activity.startActivity(Intent.createChooser(shareIntent, "Compartilhar imagem via"))
}


actual class ScreenshotManager(
    private val context: Context
) {
    actual fun shareImage(image: ImageBitmap) {
        shareImageImageBitmap2(image)
    }

    private fun shareImageImageBitmap(image: ImageBitmap) {

        // Converte o ImageBitmap para Bitmap do Android
        val bitmap = image.asAndroidBitmap()

        // Salva o Bitmap em um arquivo temporário
        val file = File(context.cacheDir, "shared_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // Cria um intent para compartilhar o arquivo
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necessário para contexts não-Activity

        }

        context.startActivity(Intent.createChooser(intent, "Compartilhar imagem").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        // Abre o share sheet
//    androidContext.startActivity(Intent.createChooser(intent, "Compartilhar imagem"))
    }

    private fun shareImageImageBitmap2(image: ImageBitmap) {
        // Converte o ImageBitmap para Bitmap do Android
        val bitmap = image.asAndroidBitmap()

        // Salva o Bitmap em um arquivo temporário
        val file = File(context.cacheDir, "shared_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // Obtém o URI do arquivo usando o FileProvider
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        // Cria um intent para compartilhar o arquivo
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Necessário para contexts não-Activity
        }

        // Abre o share sheet
        context.startActivity(Intent.createChooser(intent, "Compartilhar imagem").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

}

actual fun PlatformFile.toFirebaseFile(): FirebaseFile {
    return FirebaseFile(this.uri)
}
