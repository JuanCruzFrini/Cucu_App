package com.cucu.cucuapp.data.models.cart

import android.os.Parcelable
import com.cucu.cucuapp.data.models.Product
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartProduct(
    var documentId:String?="",
    var productId:String?="",
    var product: Product = Product(),
    var quantity:Int?=0
) : Parcelable
