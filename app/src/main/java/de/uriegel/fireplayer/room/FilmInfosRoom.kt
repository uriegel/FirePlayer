package de.uriegel.fireplayer.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(FilmInfo::class)], version = 1, exportSchema = false)
@TypeConverters(FilmInfoConverter::class)
abstract class FilmInfosRoom: RoomDatabase() {
    abstract fun filmInfoDao(): FilmInfoDao

    companion object {
        val instance: FilmInfosRoom = getDatabase()

        private fun getDatabase(): FilmInfosRoom {
            synchronized(FilmInfosRoom::class.java) {
                return Room.databaseBuilder(de.uriegel.fireplayer.Application.instance, FilmInfosRoom::class.java, "filmInfos")
                    //.addMigrations(Migration_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}