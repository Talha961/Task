package com.example.ca1.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cocktail(
    val idDrink: Int,
    val strDrink: String,
    val strInstructions: String,
    val strDrinkThumb: String,
    val strAlcoholic: String
) : Parcelable