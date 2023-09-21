package com.cucu.cucuapp.data.repository

import com.cucu.cucuapp.data.network.NotificationsDataSource
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val dataSource: NotificationsDataSource
) {
    fun getUserNotifications() = dataSource.getNotifications()
}