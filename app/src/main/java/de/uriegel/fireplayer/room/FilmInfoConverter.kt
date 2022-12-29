//package de.uriegel.fireplayer.room
//
//import androidx.room.TypeConverter
//import java.util.*
//
//class FilmInfoConverter {
//    @TypeConverter
//    fun fromTimestamp(value: Long?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: Date?): Long? {
//        return date?.time
//    }
//}
