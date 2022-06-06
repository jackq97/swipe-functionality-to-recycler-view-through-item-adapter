package com.example.happyplaces.database

import android.app.Application

class PlaceApplication: Application() {

    val db by lazy {

        PlacesDatabase.getInstance(this)

    }

}