package com.cucu.cucuapp.presentation.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val repository: ProductsRepository
): ViewModel() {

    private val _existInFavList: MutableLiveData<Boolean> = MutableLiveData()
    val existInFavList: LiveData<Boolean> = _existInFavList

    fun saveInUserHistory(productId: String){
        viewModelScope.launch {
            repository.saveInUsersHistory(productId)
        }
    }

    fun setFav(productId: String){
        viewModelScope.launch {
            try {
                val result = repository.setFav(productId)
                _existInFavList.value = result
            } catch(e:Exception){
                Log.d("Error", e.message.toString())
            }
        }
    }

    fun checkIfExistInFavList(productId: String){
        viewModelScope.launch {
            try {
               val result = repository.checkIfExistInFavList(productId)
                _existInFavList.value = result
            } catch(e:Exception){
                Log.d("Error", e.message.toString())
            }
        }
    }

    fun createPurchase(purchase: Purchase){
        viewModelScope.launch {
            try {
                repository.createPurchase(purchase)
            } catch(e:Exception){
                Log.d("Error", e.message.toString())
            }
        }
    }
    fun addToCart(productId: String, quantity:Int){
        viewModelScope.launch {
            try {
                repository.addToCart(productId, quantity)
            } catch(e:Exception){
                Log.d("Error", e.message.toString())
            }
        }
    }
}