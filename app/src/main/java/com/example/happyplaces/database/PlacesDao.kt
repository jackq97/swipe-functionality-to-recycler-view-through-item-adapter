package com.example.happyplaces.database

import androidx.room.*
import com.example.happyplaces.database.PlaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {

    @Insert
    suspend fun insert(placeEntity: PlaceEntity)

    @Update
    suspend fun update(placeEntity: PlaceEntity)

    @Delete
    suspend fun delete(placeEntity: PlaceEntity)

    @Query("SELECT * FROM `place-table`")
    fun fetchAllPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM `place-table` WHERE id = :id")
    fun fetchPlaceById(id: Int): Flow<PlaceEntity>

}