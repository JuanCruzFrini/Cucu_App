package com.cucu.cucuapp.presentation.navdrawer.purchases.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Purchase
import com.cucu.cucuapp.domain.GetPurchasesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchasesViewModel @Inject constructor(
    private val getPurchasesUseCase: GetPurchasesUseCase
) : ViewModel() {

    private val _purchases: MutableLiveData<List<Purchase>> = MutableLiveData()
    val purchases: LiveData<List<Purchase>> = _purchases


    fun getPurchases(){
        viewModelScope.launch {
            try {
                val purchases = getPurchasesUseCase.invoke()
                _purchases.postValue(purchases)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}