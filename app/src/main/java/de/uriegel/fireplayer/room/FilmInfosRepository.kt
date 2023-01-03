package de.uriegel.fireplayer.room

import kotlinx.coroutines.*

class FilmInfosRepository(val filmInfoDao: FilmInfoDao) {

    fun insertAsync(info: FilmInfo): Deferred<Unit> =
        coroutineScope.async(Dispatchers.IO) {
            return@async filmInfoDao.insert(info)
        }
    fun findAsync(name: String): Deferred<Array<FilmInfo>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async filmInfoDao.find(name)
        }
    fun getAsync(): Deferred<Array<FilmInfo>> =
        coroutineScope.async(Dispatchers.IO) {
            return@async filmInfoDao.get()
        }

    fun delete(name: String) =
        coroutineScope.launch(Dispatchers.IO) { filmInfoDao.delete(name) }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
}