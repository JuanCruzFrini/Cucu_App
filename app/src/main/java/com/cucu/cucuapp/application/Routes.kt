package com.cucu.cucuapp.application

import android.net.Uri
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.purchase.Purchase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

sealed class Routes(val route:String) {
    //mainScreen
    object Main:Routes("main")

    object Home:Routes("home")
    object Discounts:Routes("discounts")
    object Profile:Routes("profile")

    object Notifications:Routes("notifications")

    object ProductDetail: Routes("product_detail/{product}"){
        //Encodeamos el objeto a Json para que sea valido en la URL
        //En este caso tambien hay que encodearlo dentro de una Uri por que el objeto contiene
        //un parametro que es una url y hace que falle el la URL
        fun createRoute(product: Product) = "product_detail/${Uri.encode(Json.encodeToJsonElement(product).toString())}"
    }

    //navDrawer
    object Cart:Routes("cart")
    object Favorites:Routes("favorites")
    object History:Routes("history")
    object Categories:Routes("categories")
    object Purchases:Routes("purchases")

    object PurchaseDetail:Routes("purchase_detail/{purchase}"){
        fun createRoute(purchase: Purchase) = "purchase_detail/${Uri.encode(Json.encodeToJsonElement(purchase).toString())}"
    }

     object Category:Routes("category/{category}"){
        fun createRoute(category: String) = "category/$category"
    }
}