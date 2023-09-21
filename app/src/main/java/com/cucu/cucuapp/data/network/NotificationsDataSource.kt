package com.cucu.cucuapp.data.network

import com.cucu.cucuapp.data.models.Notification
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class NotificationsDataSource @Inject constructor(
    private val user: FirebaseUser?
) {
    fun getNotifications() : List<Notification> {
        return user?.let {
            emptyList()
        }
            ?: emptyList()
    }
}