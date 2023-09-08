package com.cucu.cucuapp.presentation.navdrawer.cart.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Cart
import com.cucu.cucuapp.domain.GetCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCart: GetCartUseCase
) : ViewModel() {

    private val _cart: MutableLiveData<Cart> = MutableLiveData()
    val cart: LiveData<Cart> = _cart

    fun getCart(){
        viewModelScope.launch {
            try {
                val list = getCart.invoke()
                _cart.postValue(list)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun buyCart(cart: Cart){
        viewModelScope.launch {
            try {
                //transform cart to purchase and clean cart list
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun cancelCart(){
        viewModelScope.launch {
            try {
                //clean cart list
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun removeProduct(id: Int?) {
        viewModelScope.launch {
            try {
                //remove with id
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }


}