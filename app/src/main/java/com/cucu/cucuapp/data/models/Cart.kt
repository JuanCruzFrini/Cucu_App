package com.cucu.cucuapp.data.models

import kotlin.math.roundToInt

data class Cart(
    val products: List<CartProduct>? = emptyList()
) {
    fun getAmount(): Int {
        var totalPrice = 0.00
        products?.forEach { cart ->
            val totalPerProduct = cart.product.newPrice!! * cart.quantity!!
            totalPrice += totalPerProduct
        }
        return totalPrice.roundToInt()
    }

    fun getQuantity(): Int {
        var totalQuantity = 0
        products?.forEach{ cart ->
            totalQuantity += cart.quantity!!
        }
        return totalQuantity
    }
}
