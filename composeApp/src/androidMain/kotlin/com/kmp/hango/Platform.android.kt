package com.kmp.hango

import android.os.Build
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.net.Uri
import androidx.core.content.FileProvider


class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual fun takeScreenshot(context: Any?) {
    val activity = context as Activity

    // Captura a raiz da view
    val rootView: View = activity.window.decorView.rootView
    val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    rootView.draw(canvas)

    // Salva o bitmap em um arquivo temporário
    val imageFile = saveBitmapToCache(bitmap, activity)

    if (imageFile != null) {
        // Apresenta a modal de compartilhamento com a imagem
        shareImage(imageFile, activity)
    } else {
        println("Falha ao capturar o screenshot")
    }
}

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

private fun shareImage(imageFile: File, activity: Activity) {
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

private fun saveBitmapToGallery(bitmap: Bitmap) {
    val filename = "screenshot_${System.currentTimeMillis()}.png"
    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val file = File(directory, filename)
    try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        // Notifica a galeria sobre a nova imagem
        // Você pode precisar adicionar código para atualizar a galeria
    } catch (e: IOException) {
        e.printStackTrace()
    }
}