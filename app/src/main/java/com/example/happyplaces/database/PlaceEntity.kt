package com.example.happyplaces.database

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place-table")
data class PlaceEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var date: String = "",
    var location: String = "",
    var longitude: Double = 0.0 ,
    var latitude: Double = 0.0
)