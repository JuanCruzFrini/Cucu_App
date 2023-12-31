package com.cucu.cucuapp.presentation.mainscreen.discounts.viewmodel

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
class DiscountsViewModel @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {

    private val _discountsList:MutableLiveData<List<Product>> = MutableLiveData()
    val discountsList:LiveData<List<Product>> = _discountsList

    fun getDiscounts(){
        viewModelScope.launch {
            try {
                val discounts = repository.getDiscounts()
                _discountsList.postValue(discounts)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}