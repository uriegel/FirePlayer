//package de.uriegel.fireplayer.room
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//
//@Dao
//interface FilmInfoDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(info: FilmInfo)
//
//    @Query("SELECT * FROM FilmInfos WHERE name = :name")
//    fun find(name: String): Array<FilmInfo>
//
//    @Query("SELECT * FROM FilmInfos")
//    fun get(): Array<FilmInfo>
//
//    @Query("DELETE FROM FilmInfos WHERE name = :name")
//    fun delete(name: String)
//}