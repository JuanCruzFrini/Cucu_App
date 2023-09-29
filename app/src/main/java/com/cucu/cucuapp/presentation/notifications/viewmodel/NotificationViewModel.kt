package com.cucu.cucuapp.presentation.notifications.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cucu.cucuapp.data.models.Notification
import com.cucu.cucuapp.data.repository.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repo: NotificationsRepository
) : ViewModel() {

    private val _notifications: MutableLiveData<List<Notification>> = MutableLiveData()
    val notifications: LiveData<List<Notification>> = _notifications

    fun getNotificationsList() {
        viewModelScope.launch {
            try {
                val notifications = repo.getUserNotifications()
                _notifications.value = notifications
            } catch (e:Exception){
                Log.d("Error not", e.message.toString())
            }
        }
    }

    fun updateNotificationState(id:String){
        viewModelScope.launch {
            try {
                repo.updateNotificationState(id)
            } catch (e:Exception){
                Log.d("Error updating not", e.message.toString())
            }
        }
    }
}