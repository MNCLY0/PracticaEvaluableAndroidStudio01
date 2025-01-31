package com.example.prcticaevaluableapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class ManejadorImagenes {

    // Convertimos un byteArray a un Bitmap para mostrar en un ImageView
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // Convertimos un ImageView a un ByteArray para almacenar en la base de datos
    fun imagenToByteArray(imageView: ImageView): ByteArray {
        val drawable = imageView.drawable
        if (drawable != null && drawable is BitmapDrawable) {
            //Como el cursor de la base de datos no puede almacenar más de 1MB, comprimimos la imagen a 0.5MB para que no haya problemas
            val bitmap =  drawable.bitmap
            var quality = 100
            do {
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
                val byteArray = stream.toByteArray()
                if (byteArray.size <= 500 * 1024) {
                    Log.e("ManejadorImagenes", "El tamaño de la imagen ahora mismo es de ${byteArray.size} bytes")
                    return byteArray
                }
                quality -= 10
            } while (quality > 0)
            Log.e("ManejadorImagenes", "No se pudo comprimir la imagen a menos de 0.5MB")
            return ByteArray(0)
        } else {
            Log.e("ManejadorImagenes", "Error al convertir la imagen a ByteArray, es null")
            return ByteArray(0)
        }
    }
    // Convertimos un ByteArray a un Drawable
    fun byteArrayToDrawable(context: Context, byteArray: ByteArray): BitmapDrawable {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        return BitmapDrawable(context.resources, bitmap)
    }

}