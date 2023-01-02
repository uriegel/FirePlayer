package de.uriegel.fireplayer.requests

import de.uriegel.fireplayer.extensions.isFilm
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Files(val files: List<String>)

suspend fun getFilmList(url: String): Result<List<String>> {
    val folderComparator = compareBy<String>{ it.isFilm() }
    val fileTypeThenStringComparator = folderComparator.thenBy { it }

    fun getFilmList(stringResult: String) =
        Json
            .decodeFromString<Files>(stringResult)
            .files
            .sortedWith(fileTypeThenStringComparator)
            .toList()

    return getString(
        url
            .replace("+", "%20"))
        .map { getFilmList(it) }
}