package de.uriegel.fireplayer.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FilmInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilmInfo(info: FilmInfo)

    @Query("SELECT * FROM FilmInfos WHERE name = :name")
    fun findFilmInfo(name: String): Array<FilmInfo>
}