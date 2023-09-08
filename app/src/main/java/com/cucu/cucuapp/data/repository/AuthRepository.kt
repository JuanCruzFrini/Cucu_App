package com.cucu.cucuapp.data.repository

import com.cucu.cucuapp.data.network.AuthDataSource
import com.google.firebase.auth.AuthCredential
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val dataSource: AuthDataSource
) {
    suspend fun signIn(credential: AuthCredential) = dataSource.signIn(credential)

    fun signOut() = dataSource.signOut()
}