package de.uriegel.fireplayer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [(FilmInfo::class)], version = 1, exportSchema = false)
@TypeConverters(FilmInfoConverter::class)
abstract class FilmInfosRoom: RoomDatabase() {
    abstract fun filmInfoDao(): FilmInfoDao

    companion object {
        private var instance: FilmInfosRoom? = null

        fun getDatabase(context: Context): FilmInfosRoom {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext, FilmInfosRoom::class.java, "filmInfos")
                        //.addMigrations(Migration_1_2)
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance!!
            }
        }
    }
}