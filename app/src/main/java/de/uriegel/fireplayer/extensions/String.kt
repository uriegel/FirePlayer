package de.uriegel.fireplayer.extensions

import android.util.Base64

fun String.isFilm() =
    this.endsWith(".mp4", true) or this.endsWith(".mkv", true)

fun String.isMusic() =
    this.endsWith(".mp3", true) or this.endsWith(".ogg", true)

fun String.isFolder() = !isFilm() && !isMusic()

fun String.getTitle() = this.substringBeforeLast('.')

fun String.toBase64() = Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)!!
fun String.fromBase64() = String(Base64.decode(this, Base64.DEFAULT))
