package com.cucu.cucuapp.presentation.mainscreen.profile.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.User
import com.cucu.cucuapp.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repo: AuthRepository
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

    fun signIn(credential: AuthCredential){
        viewModelScope.launch {
            try {
                val user = repo.signIn(credential)
                this@LoginViewModel._user.postValue(user)
            } catch (e: Exception) {
                Log.d("Error login", e.message.toString())
            }
        }
    }

    fun signOut(){
        viewModelScope.launch { repo.signOut() }
    }
}