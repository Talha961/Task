package com.example.ca1.api
import com.example.ca1.model.CocktailResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {

    @GET("search.php")
    suspend fun getDrinks(@Query("s") searchQuery: String): CocktailResponse

    @GET("search.php")
    suspend fun getDrinksByAlphabet(@Query("f") searchQuery: String): CocktailResponse

}