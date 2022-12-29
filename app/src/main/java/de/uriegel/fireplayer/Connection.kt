//package de.uriegel.fireplayer
//
//import android.util.Log
//import de.uriegel.activityextensions.http.getString
//import kotlinx.serialization.ExperimentalSerializationApi
//
//@ExperimentalSerializationApi
//suspend fun accessDisk() {
//    try {
//        getString("${MainActivity.url}/accessdisk")
//    } catch (e: Exception) {
//        Log.w("FP", "ListItems", e)
//    }
//}
//
//@ExperimentalSerializationApi
//suspend fun diskNeeded() {
//    try {
//        getString("${MainActivity.url}/diskneeded")
//    } catch (e: Exception) {
//        Log.w("FP", "ListItems", e)
//   }
//}