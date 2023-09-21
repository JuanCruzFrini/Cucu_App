package com.cucu.cucuapp.presentation.navdrawer.history.viewmodel

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
class HistoryViewModel @Inject constructor(
    private val repository: ProductsRepository
): ViewModel() {

    private val _history: MutableLiveData<List<Product>> = MutableLiveData()
    val history: LiveData<List<Product>> = _history
    fun getUsersHistory(){
        viewModelScope.launch {
            try {
                val historyList = repository.getHistory()
                _history.postValue(historyList)
            } catch (e:Exception){
                Log.d("Error", e.message.toString())
            }
        }
    }

}