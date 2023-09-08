package com.cucu.cucuapp.presentation.mainscreen.profile.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.domain.GetFavoritesUseCase
import com.cucu.cucuapp.domain.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _favorites: MutableLiveData<List<Product>> = MutableLiveData()
    val favorites: LiveData<List<Product>> = _favorites

    fun getFavorites(){
        viewModelScope.launch {
            try {
                val favorites = getFavoritesUseCase.invoke()
                _favorites.postValue(favorites)
            } catch (e:Exception){
                Log.e("Error", e.message.toString())
            }
        }
    }

    private val _history: MutableLiveData<List<Product>> = MutableLiveData()
    val history: LiveData<List<Product>> = _history

    fun getUserHistory(){
        viewModelScope.launch {
            try {
                val history = getHistoryUseCase.invoke()
                _history.postValue(history)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}