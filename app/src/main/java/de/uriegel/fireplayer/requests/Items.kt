package de.uriegel.fireplayer.requests

import de.uriegel.fireplayer.extensions.append
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Files(
    val directories: List<String>,
    val files:       List<String>)

data class DirectoryItem(
    val name:        String,
    val isDirectory: Boolean,
    val thumbnail: String?)

fun getAsDirectoryItems(dirs: List<String>, files: List<String>, thumbnailDirPath: String?) =
    dirs.map { DirectoryItem(it, true, null) } +
            files.map {
                DirectoryItem(it, false, thumbnailDirPath?.append("/$it"))
            }

suspend fun getItemList(url: String): Result<Files> {
    fun getItemList(stringResult: String) =
        Json.decodeFromString<Files>(stringResult)

    return getString(url.replace("+", "%20"))
        .map { getItemList(it) }
}