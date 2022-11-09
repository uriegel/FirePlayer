package de.uriegel.fireplayer.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "FilmInfos")
data class FilmInfo (
    val position: Long = 0,
    val date: Date,
    @PrimaryKey
    val name: String,
)

