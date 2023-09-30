package com.cucu.cucuapp.presentation.mainscreen.profile.profile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.User
import com.cucu.cucuapp.domain.GetFavoritesUseCase
import com.cucu.cucuapp.domain.GetHistoryUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _user:MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    fun authListener(){
        auth.addAuthStateListener {
            if (it.currentUser == null) {
                _user.postValue(null)
            } else {
                _user.postValue(
                    User(
                        id = it.currentUser?.uid,
                        name = it.currentUser?.displayName,
                        img = it.currentUser?.photoUrl.toString()
                    )
                )
            }
        }
    }

    private val _favorites: MutableLiveData<List<Product>> = MutableLiveData()
    val favorites: LiveData<List<Product>> = _favorites

    private val _loadingFavorites: MutableLiveData<Boolean> = MutableLiveData()
    val loadingFavorites: LiveData<Boolean> = _loadingFavorites

    fun getFavorites(){
        viewModelScope.launch {
            _loadingFavorites.value = true
            try {
                val favorites = getFavoritesUseCase.invoke()
                _favorites.postValue(favorites)
                _loadingFavorites.value = false
            } catch (e:Exception){
                Log.e("Error", e.message.toString())
                _loadingFavorites.value = false
            }
        }
    }

    private val _history: MutableLiveData<List<Product>> = MutableLiveData()
    val history: LiveData<List<Product>> = _history

    private val _loadingHistory: MutableLiveData<Boolean> = MutableLiveData()
    val loadingHistory: LiveData<Boolean> = _loadingHistory

    fun getUserHistory(){
        viewModelScope.launch {
            _loadingFavorites.value = true
            try {
                val history = getHistoryUseCase.invoke()
                _history.postValue(history)
                _loadingFavorites.value = false
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
                _loadingFavorites.value = false
            }
        }
    }
}