package de.uriegel.fireplayer.request

import de.uriegel.fireplayer.exceptions.HttpProtocolException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.GZIPInputStream

suspend fun getResponseStream(urlString: String) =
    runCatching { tryGetResponseStream(urlString) }

private suspend fun tryGetResponseStream(urlString: String): InputStream {
    return withContext(Dispatchers.IO) {
        return@withContext getResponseStreamSync(urlString)
    }
}

private fun getResponseStreamSync(urlString: String): InputStream {
    val url = URL(urlString)
    val connection = url.openConnection() as HttpURLConnection
    connection.setRequestProperty("Accept-Encoding", "gzip")
    connection.connect()
    val result = connection.responseCode
    if (result != 200)
        throw HttpProtocolException(result, connection.responseMessage)
    return if (connection.contentEncoding == "gzip")
        GZIPInputStream(connection.inputStream)
    else
        connection.inputStream
}