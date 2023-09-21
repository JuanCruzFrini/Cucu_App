package com.cucu.cucuapp.presentation.navdrawer.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: ProductsRepository
): ViewModel() {

    private val _favList: MutableLiveData<List<Product>> = MutableLiveData()
    val favList: LiveData<List<Product>> = _favList
    fun getFavorites(){
        viewModelScope.launch {
            try {
                val favorites = repository.getFavorites()
                _favList.postValue(favorites)
            } catch (e:Exception){
                Log.d("Error", e.message.toString())
            }

        }
    }
}