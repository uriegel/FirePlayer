package de.uriegel.fireplayer.requests

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
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

fun initializeHttp(context: Context) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    preferences.getString("url", "")?.let {
        if (it.length  >= 6)
            url = it

        basicAuthentication(
            preferences.getString("name", "")!!,
            preferences.getString("auth_pw", "")!!)
    }
}

suspend fun getString(urlString: String): String {

    return withContext(Dispatchers.IO) {
        val url = URL(url + urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Accept-Encoding", "gzip")
        connection.connect()
        val result = connection.responseCode
        if (result != 200)
            throw Exception("$result ${connection.responseMessage}")
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

private lateinit var url: String