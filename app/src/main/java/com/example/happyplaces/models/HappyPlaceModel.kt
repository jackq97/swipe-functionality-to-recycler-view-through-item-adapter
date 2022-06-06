package com.example.happyplaces.models

data class HappyPlaceModel(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val image: String,
    val date: String,
    val longitude: Double,
    val latitude: Double
)