package de.uriegel.fireplayer

import android.util.Log
import android.widget.Toast
import de.uriegel.activityextensions.http.getString
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
suspend fun registerDisk() {
    try {
        getString("${MainActivity.url}/registerdisk")
    } catch (e: Exception) {
        Log.w("FP", "ListItems", e)
    }
}

@ExperimentalSerializationApi
suspend fun unregisterDisk() {
    try {
        getString("${MainActivity.url}/unregisterdisk")
    } catch (e: Exception) {
        Log.w("FP", "ListItems", e)
   }
}