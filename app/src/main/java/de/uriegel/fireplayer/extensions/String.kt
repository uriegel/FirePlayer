package de.uriegel.fireplayer.extensions

import android.util.Base64

fun String.isFilmPath() =
    this.startsWith("/video")

fun String.isMusicPath() =
    this.startsWith("/music")

fun String.isPicturePath() =
    this.startsWith("/pics")

fun String.isMusic() =
    this.endsWith(".mp3", true) or this.endsWith(".ogg", true)

fun String.isPicture() =
    this.endsWith(".jpg", true) or
            this.endsWith(".png", true) or
            this.endsWith(".mp4", true)

fun String.getTitle() = this.substringBeforeLast('.')

fun String.getFilePath() =
    this.replaceAfterLast('/', "")

fun String.toBase64() = Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)!!
fun String.fromBase64() = String(Base64.decode(this, Base64.DEFAULT))

fun String.append(text: String) = this + text
