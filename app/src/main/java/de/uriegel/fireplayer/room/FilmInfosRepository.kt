//package de.uriegel.fireplayer.room
//
//import kotlinx.coroutines.*
//
//object FilmInfosRepository {
//
//    fun insert(info: FilmInfo): Deferred<Unit> =
//        coroutineScope.async(Dispatchers.IO) {
//            return@async filmInfoDao.insert(info)
//        }
//    fun find(name: String): Deferred<Array<FilmInfo>> =
//        coroutineScope.async(Dispatchers.IO) {
//            return@async filmInfoDao.find(name)
//        }
//    fun get(): Deferred<Array<FilmInfo>> =
//        coroutineScope.async(Dispatchers.IO) {
//            return@async filmInfoDao.get()
//        }
//
//    fun delete(name: String) =
//        coroutineScope.launch(Dispatchers.IO) { filmInfoDao.delete(name) }
//
//    private val filmInfoDao: FilmInfoDao
//    private val coroutineScope = CoroutineScope(Dispatchers.Main)
//
//    init {
//        val db : FilmInfosRoom = FilmInfosRoom.instance
//        filmInfoDao = db.filmInfoDao()
//    }
//}