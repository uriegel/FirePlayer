package de.uriegel.fireplayer.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import de.uriegel.fireplayer.room.FilmInfo
import de.uriegel.fireplayer.room.FilmInfosRepository
import de.uriegel.fireplayer.room.FilmInfosRoom
import kotlinx.coroutines.*

class VideoViewModel(application: Application): ViewModel() {

    fun findAsync(path: String): Deferred<Array<FilmInfo>> = repository.findAsync(path)

    fun insert(info: FilmInfo) {
        coroutineScope.launch {
            repository
                .insertAsync(info)
                .await()

            repository.getAsync()
                .await()
                .sortedWith(compareByDescending { it.date })
                .drop(30)
                .map { it.name }
                .forEach { repository.delete(it) }
        }

    }

    private val repository: FilmInfosRepository

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        val videoDb = FilmInfosRoom.getDatabase(application)
        val filmDao = videoDb.filmInfoDao()
        repository = FilmInfosRepository(filmDao)
    }
}