package com.cucu.cucuapp.presentation.navdrawer.cart.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.cart.Cart
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val _cart: MutableLiveData<Cart> = MutableLiveData()
    val cart: LiveData<Cart> = _cart

    fun getCart(){
        viewModelScope.launch {
            try {
                val cart = repository.getCart()
                _cart.postValue(cart)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun buyCart(purchase: Purchase){
        viewModelScope.launch {
            try {
                repository.buyCart(purchase)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun clearCart(){
        viewModelScope.launch {
            try {
                repository.clearCart()
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun removeProduct(documentId: String) {
        viewModelScope.launch {
            try {
                repository.removeCartProduct(documentId)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun editProductQuantity(documentId: String, newQuantity: Int){
        viewModelScope.launch {
            try {
                repository.editCartProductQuantity(documentId, newQuantity)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}