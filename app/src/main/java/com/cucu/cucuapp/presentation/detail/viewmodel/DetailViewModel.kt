package com.cucu.cucuapp.presentation.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.User
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.repository.ProductsRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    val repository: ProductsRepository,
): ViewModel() {

    private val _user:MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    private val _purchaseId:MutableLiveData<String?> = MutableLiveData()
    val purchaseId: LiveData<String?> = _purchaseId

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

    private val _existInFavList: MutableLiveData<Boolean> = MutableLiveData()
    val existInFavList: LiveData<Boolean> = _existInFavList

    fun saveInUserHistory(productId: String){
        viewModelScope.launch {
            repository.saveInUsersHistory(productId)
        }
    }
    fun increaseSeenTimes(product: Product) {
        viewModelScope.launch {
            repository.increaseSeenTimes(product)
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
                val purchaseId = repository.createPurchase(purchase)
                _purchaseId.value = purchaseId
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