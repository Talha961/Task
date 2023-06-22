package com.example.ca1.localDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ca1.data.FavouriteEntity

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: FavouriteEntity)

    @Query("SELECT * FROM favourites WHERE id = :id")
    suspend fun getFavouriteById(id: Int): FavouriteEntity?


    @Query("DELETE FROM favourites WHERE id = :id")
    suspend fun removeFavourite(id: Int)

    @Query("SELECT * FROM favourites")
    suspend fun getAll(): MutableList<FavouriteEntity?>?

}