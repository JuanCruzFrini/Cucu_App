package com.cucu.cucuapp.data.repository

import com.cucu.cucuapp.data.network.NotificationsDataSource
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val dataSource: NotificationsDataSource
) {
    suspend fun getUserNotifications() = dataSource.getNotifications()
    suspend fun updateNotificationState(id:String) = dataSource.updateNotificationState(id)
    suspend fun getNotificationsNotOpen() = dataSource.getNotificationsNotOpen()
}