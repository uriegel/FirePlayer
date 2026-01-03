package de.uriegel.fireplayer

import android.content.Context
import android.util.Log
import fi.iki.elonen.NanoHTTPD

class WebServer(private val context: Context, port: Int = 8080) : NanoHTTPD(port) {
    override fun serve(session: IHTTPSession): Response {
        val uriPath = session.uri.trimStart('/')
        val assetPath = uriPath.ifEmpty { "index.html" }
        Log.d("Nano", "Aufruf: $uriPath")
        return try {
            val inputStream = context.assets.open(assetPath)
            //val inputStream = context.assets.open("$assetsDir/$assetPath")
            val mime = getMimeType(assetPath)
            newFixedLengthResponse(Response.Status.OK, mime, inputStream, inputStream.available().toLong())
        } catch (_: Exception) {
            newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found")
        }
    }

    private fun getMimeType(path: String): String {
        return when {
            path.endsWith(".html") -> "text/html"
            path.endsWith(".js") -> "application/javascript"
            path.endsWith(".css") -> "text/css"
            path.endsWith(".png") -> "image/png"
            path.endsWith(".jpg") || path.endsWith(".jpeg") -> "image/jpeg"
            path.endsWith(".svg") -> "image/svg+xml"
            else -> "application/octet-stream"
        }
    }

    // private val assetsDir = "" // folder in assets containing your built React app
}