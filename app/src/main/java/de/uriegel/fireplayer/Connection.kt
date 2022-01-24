package de.uriegel.fireplayer

import de.uriegel.activityextensions.http.getString
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
suspend fun checkMediaDevice() {
    getString("${MainActivity.url}/access")
}