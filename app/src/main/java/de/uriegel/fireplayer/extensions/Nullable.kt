package de.uriegel.fireplayer.extensions

fun <T> T?.toResult(toException: ()->Exception) =
    if (this == null)
        Result.failure(toException())
    else
        Result.success(this)

