package de.uriegel.fireplayer.requests

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import de.uriegel.fireplayer.exceptions.HttpProtocolException
import de.uriegel.fireplayer.exceptions.NotInitializedException
import de.uriegel.fireplayer.extensions.sideEffect
import de.uriegel.fireplayer.extensions.toResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Authenticator
import java.net.HttpURLConnection
import java.net.PasswordAuthentication
import java.net.URL
import java.util.zip.GZIPInputStream

fun basicAuthentication(name: String, pw: String) {
    class BasicAuthenticator : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(name, pw.toCharArray())
        }
    }
    Authenticator.setDefault(BasicAuthenticator())
}

fun initializeHttp(context: Context) =
    PreferenceManager
        .getDefaultSharedPreferences(context)
        .getSettings()
        .initializeHttp { url = it }

private fun Settings.initializeHttp(setUrl: (String)->Unit) =
    this.url
            .let {
                if (it.length >= 8 && it.startsWith("http", true))
                    it
                else
                    null }
            ?.let {
                if (this.name.isNotEmpty() && this.pw.isNotEmpty())
                    basicAuthentication(this.name, this.pw)
                it }
            .toResult { NotInitializedException() }
            .sideEffect(setUrl)
            .map { }

suspend fun getString(urlString: String) =
    runCatching { tryGetString(urlString) }

private suspend fun tryGetString(urlString: String): String {

    return withContext(Dispatchers.IO) {
        val url = URL(url + urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Encoding", "gzip")
        connection.connect()
        val result = connection.responseCode
        if (result != 200)
            throw HttpProtocolException(result, connection.responseMessage)
        val inStream =
            if (connection.contentEncoding == "gzip")
                GZIPInputStream(connection.inputStream)
            else
                connection.inputStream
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

private fun SharedPreferences.getSettings() =
    Settings(
        this.getString("url", "")!!,
        this.getString("url", "")!!,
        this.getString("url", "")!!
    )

private data class Settings(
    val url: String,
    val name: String,
    val pw: String
)

private lateinit var url: String