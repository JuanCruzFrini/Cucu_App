package com.cucu.cucuapp.presentation.navdrawer.categories.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.items.ItemCategory
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.domain.GetCategoriesUseCase
import com.cucu.cucuapp.domain.GetProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _categories: MutableLiveData<List<ItemCategory>> = MutableLiveData()
    val categories: LiveData<List<ItemCategory>> = _categories

    private val _productsByCategory: MutableLiveData<List<Product>> = MutableLiveData()
    val productsByCategory: LiveData<List<Product>> = _productsByCategory


    fun getCategories(){
        viewModelScope.launch {
            try {
                val list = getCategoriesUseCase.invoke()
                _categories.postValue(list)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun getCategory(category:String){
        viewModelScope.launch {
            try {
                val filteredList = getCategoryUseCase.getProductsByCategory(category)
                _productsByCategory.postValue(filteredList)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}