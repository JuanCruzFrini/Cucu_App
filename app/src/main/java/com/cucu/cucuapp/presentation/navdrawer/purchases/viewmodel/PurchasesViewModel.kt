package com.cucu.cucuapp.presentation.navdrawer.purchases.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.repository.ProductsRepository
import com.cucu.cucuapp.domain.GetPurchasesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchasesViewModel @Inject constructor(
    private val getPurchasesUseCase: GetPurchasesUseCase,
    private val repository:ProductsRepository
) : ViewModel() {

    private val _purchases: MutableLiveData<List<Purchase>> = MutableLiveData()
    val purchases: LiveData<List<Purchase>> = _purchases

    private val _purchase: MutableLiveData<Purchase> = MutableLiveData()
    val purchase: LiveData<Purchase> = _purchase

    fun getPurchases(){
        viewModelScope.launch {
            try {
                val purchases = /*repository.getPurchasesReferences()*/getPurchasesUseCase.invoke()
                _purchases.postValue(purchases)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun getPurchaseById(purchaseId:String){
        viewModelScope.launch {
            try {
                val purchase = repository.getPurchaseById(purchaseId)
                _purchase.value = purchase
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
    fun cancelPurchase(purchase: Purchase){
        viewModelScope.launch {
            try {
                repository.cancelPurchase(purchase)
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }
}