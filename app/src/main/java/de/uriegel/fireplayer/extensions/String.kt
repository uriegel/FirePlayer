package de.uriegel.fireplayer.extensions

import android.util.Base64

fun String.isFilm(): Boolean {
    return this.endsWith(".mp4", true) or this.endsWith(".mkv", true)
}

fun String.isFolder(): Boolean {
    return !this.contains(".")
}

fun String.getTitle() = this.substringBeforeLast('.')

fun String.toBase64() = Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)!!
fun String.fromBase64() = String(Base64.decode(this, Base64.DEFAULT))
