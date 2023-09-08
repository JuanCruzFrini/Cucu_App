package com.cucu.cucuapp.data.network

import android.util.Log
import com.cucu.cucuapp.data.models.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun signIn(credential:AuthCredential): User? {
        return if (auth.currentUser == null){
            try {
                val result = auth.signInWithCredential(credential).await().user
                User(id = result?.uid,
                    name = result?.displayName,
                    img = result?.photoUrl.toString())
            } catch (e:Exception){
                Log.d("Error login", e.message.toString() )
                null
            }
        } else {
            User(id = auth.currentUser?.uid,
                name = auth.currentUser?.displayName,
                img = auth.currentUser?.photoUrl.toString())
        }
    }

     fun signOut() = auth.signOut()
}