package de.uriegel.fireplayer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Authenticator
import java.net.HttpURLConnection
import java.net.PasswordAuthentication
import java.net.URL
import java.util.*
import java.util.zip.GZIPInputStream


fun basicAuthentication(name: String, pw: String) {
    class BasicAuthenticator : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(name, pw.toCharArray())
        }
    }
    Authenticator.setDefault(BasicAuthenticator())
}


@Suppress("BlockingMethodInNonBlockingContext")
suspend fun httpGet(urlString: String): String {

    return withContext(Dispatchers.IO) {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Encoding", "gzip")
        connection.connect()
        val inStream = GZIPInputStream(connection.inputStream)
        return@withContext readStream(inStream)
    }
}

private fun readStream(inString: InputStream): String {
    val response = StringBuffer()
    val reader = BufferedReader(InputStreamReader(inString))
    var line: String?
    while (reader.readLine().also { line = it } != null)
        response.append(line)
    reader.close()
    return response.toString()
}

