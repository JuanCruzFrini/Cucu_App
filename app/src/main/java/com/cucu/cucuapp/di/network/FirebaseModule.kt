package com.cucu.cucuapp.di.network

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth():FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseUser() : FirebaseUser? = FirebaseAuth.getInstance().currentUser

    @Provides
    @Singleton
    fun providesFirestore():FirebaseFirestore = FirebaseFirestore.getInstance()
}