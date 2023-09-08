package com.cucu.cucuapp.data.models

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.Date
import kotlin.math.roundToInt

@Parcelize
@Serializable
data class Purchase(
    val id:String? = "",
    val products:List<CartProduct>? = emptyList(),
    val state: PurchaseState? = PurchaseState.PendingPayment(),
) : Parcelable {

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

@Parcelize
@Serializable
sealed class PurchaseState(
    val description:String,

    @ServerTimestamp @Contextual
    val date:Date? = null
) : Parcelable {

    @Parcelize @Serializable
    class PendingPayment : PurchaseState("Pago pendiente")
    @Parcelize @Serializable
    class PaymentPendingConfirmation : PurchaseState("Pago pendiente de confirmacion")
    @Parcelize @Serializable
    class PaymentConfirmed : PurchaseState("Pago confirmado"/*, date*/)
    @Parcelize @Serializable
    class PreparingProducts : PurchaseState("Preparando productos")
    @Parcelize @Serializable
    class ReadyForPickup : PurchaseState("Listo para retirar"/*, date*/)
    @Parcelize @Serializable
    class Delivered : PurchaseState("Entregado"/*, date*/)
    @Parcelize @Serializable
    class Cancelled : PurchaseState("Cancelado"/*, date*/)
}

data class Promotion(
    val products: List<Product>? = emptyList(),
    val amount: Int? = 0
)