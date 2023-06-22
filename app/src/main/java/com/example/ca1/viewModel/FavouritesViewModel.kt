package com.example.ca1.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.ca1.data.FavouriteEntity
import com.example.ca1.model.Cocktail
import com.example.plantapp.localDB.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel (app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app)

    val _favourites: MutableLiveData<MutableList<FavouriteEntity?>?> = MutableLiveData()

    val favourites: LiveData<MutableList<FavouriteEntity?>?>
        get() = _favourites

    var tempFavourites = _favourites.value

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _cocktails: MutableLiveData<List<Cocktail>> = MutableLiveData()
    val cocktails: LiveData<List<Cocktail>>
        get() = _cocktails

    init{
        getFavourites()
    }
    private fun getFavourites(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val favourite =
                    database?.favouriteDao()?.getAll()

                favourite?.let {
                    _favourites.postValue(it)
                }
            }
        }
    }
    fun removeFavourite(favouriteEntity: FavouriteEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.favouriteDao()?.removeFavourite(favouriteEntity.id)
                tempFavourites?.remove(favouriteEntity)
                _favourites.postValue(tempFavourites)
            }
        }
    }

}