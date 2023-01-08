package de.uriegel.fireplayer.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

fun createTempFile(prefix: String, suffix: String) =
    File
        .createTempFile(prefix, suffix)
        .also {
            it.deleteOnExit()
        }

suspend fun InputStream.jpgToTempFile() =
    this.toTempFile("pic", "jpg")

suspend fun InputStream.toTempFile(prefix: String, suffix: String) =
    runCatching { tryToTempFile(prefix, suffix) }

private suspend fun InputStream.tryToTempFile(prefix: String, suffix: String): File
{
    val inputStream = this
    return withContext(Dispatchers.IO) {
        return@withContext createTempFile(prefix, suffix)
            .also {
                it.outputStream()
                .use {
                    inputStream.copyTo(it)
                }
            }
    }
}

