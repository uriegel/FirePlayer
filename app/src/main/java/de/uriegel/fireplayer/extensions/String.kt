package de.uriegel.fireplayer.extensions

fun String.isFilm(): Boolean {
    return this.endsWith(".mp4", true) or this.endsWith(".mkv", true)
}

fun String.isFolder(): Boolean {
    return !this.contains(".")
}

fun String.getTitle() = this.substringBeforeLast('.')

