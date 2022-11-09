package de.uriegel.fireplayer.room

import kotlinx.coroutines.*

object FilmInfosRepository {

    fun insertFilmInfoAsync(info: FilmInfo): Deferred<Unit> =
        coroutineScope.async(Dispatchers.IO) {
            return@async filmInfoDao.insertFilmInfo(info)
        }
    fun getFilmInfoAsync(name: String): Deferred<Array<FilmInfo>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async filmInfoDao.findFilmInfo(name)
        }
    private val filmInfoDao: FilmInfoDao
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    init {
        val db : FilmInfosRoom = FilmInfosRoom.instance
        filmInfoDao = db.filmInfoDao()
    }
}