package com.cucu.cucuapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    val id:Int?=0,
    val name:String?="",
    val newPrice:Double?=0.0,
    val oldPrice:Double?=0.0,
    val stock:Int?=0,
    val img:String?="",
    val description:String?="",
    val code:Long?=0L,
    val isDiscount:Boolean?= false,
    val category: ItemCategory = ItemCategory()
) : Parcelable