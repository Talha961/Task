package com.example.ca1.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "favourites")
data class FavouriteEntity(
    @PrimaryKey val id: Int,
    val strDrink: String,
    val strInstructions: String,
    val strAlcoholic: String,
    val strDrinkThumb: String,
)
