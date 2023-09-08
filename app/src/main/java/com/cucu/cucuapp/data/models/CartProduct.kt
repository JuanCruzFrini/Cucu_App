package com.cucu.cucuapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CartProduct(
    var product: Product,
    var quantity:Int?=0
) : Parcelable
