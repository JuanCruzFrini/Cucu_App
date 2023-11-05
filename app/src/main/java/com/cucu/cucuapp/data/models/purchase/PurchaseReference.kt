package com.cucu.cucuapp.data.models.purchase

import com.cucu.cucuapp.application.NullableDateSerializer
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import java.util.Date

data class PurchaseReference(
    val documentId:String?="",
    val uid:String?="",
    val amount: Int? = 0,
    val productsQuantity:Int? = 0,
    @ServerTimestamp @Serializable(with = NullableDateSerializer::class)
    val date: Date? = null
)