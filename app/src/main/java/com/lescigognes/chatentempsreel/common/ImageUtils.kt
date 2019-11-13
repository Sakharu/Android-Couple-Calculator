package com.lescigognes.chatentempsreel.common

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageUtils
{
    companion object
    {
        var derniereImage = ""
        fun resizeBitmap(imageAResize: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap
        {
            var image = imageAResize
            if (maxHeight > 0 && maxWidth > 0)
            {
                val width = image.width
                val height = image.height
                val ratioBitmap = width.toFloat() / height.toFloat()
                val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

                var finalWidth = maxWidth
                var finalHeight = maxHeight
                if (ratioMax > ratioBitmap)
                    finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
                else
                    finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()

                image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            }
            return image
        }

        fun bitmapToString(bitmap: Bitmap): String
        {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }

        fun exifToDegrees( exifOrientation : Int) : Int
        {
            return when (exifOrientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        }

    }
}