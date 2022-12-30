package de.uriegel.fireplayer.exceptions

class HttpProtocolException(val code: Int, message: String)
    : Exception(message) {}