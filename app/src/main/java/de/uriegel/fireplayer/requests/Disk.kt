package de.uriegel.fireplayer.requests

import android.util.Log

//@ExperimentalSerializationApi
suspend fun accessDisk() {
    try {
        getString("/accessdisk")
    } catch (e: Exception) {
        Log.w("FP", "ListItems", e)
    }
}