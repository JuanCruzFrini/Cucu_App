package com.cucu.cucuapp.data.models

import android.os.Parcelable
import com.cucu.cucuapp.data.models.items.ItemCategory
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    @get:Exclude
    var id:String?="",
    val name:String?="",
    val newPrice:Double?=0.0,
    val oldPrice:Double?=0.0,
    val stock:Int?=0,
    val img:String?="",
    val description:String?="",
    val code:Long?=0L,
    val isDiscount:Boolean?= false,
    val category: ItemCategory? = ItemCategory()
) : Parcelable {

  /*  fun isDiscount(): Boolean{
        return if (newPrice!=null && oldPrice!=null){
            newPrice.toInt() < oldPrice.toInt()
        } else false
    }*/
}