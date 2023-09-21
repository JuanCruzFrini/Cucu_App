package com.cucu.cucuapp.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Notification(
    val id:String?="",
    val title:String?="",
    val content:String?="",
    @ServerTimestamp
    val date: Date? = null,
    val hasBeenOpen:Boolean? = false
)
