package com.cucu.cucuapp.data.network

import com.cucu.cucuapp.application.Constants
import com.cucu.cucuapp.data.models.Notification
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NotificationsDataSource @Inject constructor(
    private val user: FirebaseUser?,
    private val db: FirebaseFirestore
) {
    suspend fun getNotifications() : List<Notification> {
        val notifications = mutableListOf<Notification>()

        user?.let {
            val fetch = db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.NOTIFICATIONS_COLL)
                .orderBy("date", Query.Direction.DESCENDING)
                .get().await()

            fetch.documents.forEach { document ->
                val notification = document.toObject(Notification::class.java)
                notification?.id = document.id
                notification?.let { notifications.add(it) }
            }
        }

        return notifications
    }

    suspend fun updateNotificationState(notificationId:String){
        user?.let {
            db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.NOTIFICATIONS_COLL)
                .document(notificationId).update(
                    "hasBeenOpen", true
                ).await()
        }
    }
    suspend fun getNotificationsNotOpen(): Int {
        var notificationsNotOpenQuant = 0
        user?.let {
            notificationsNotOpenQuant = db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.NOTIFICATIONS_COLL)
                .whereEqualTo("hasBeenOpen", false)
                .get().await().size()

            //notificationsNotOpenQuant = fetch.documents.size
        }

        return notificationsNotOpenQuant
    }
}