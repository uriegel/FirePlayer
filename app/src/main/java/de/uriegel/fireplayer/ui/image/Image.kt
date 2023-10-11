package de.uriegel.fireplayer.ui.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface

data class ImageData(
    val bitmap: Bitmap,
    val angle: Float
)

fun loadImageData(bitmapBytes: ByteArray): ImageData {
    val angle = bitmapBytes.inputStream().use {
        val exif = ExifInterface(it)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            else -> 0f
        }
    }
    return ImageData(BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size), angle)
}