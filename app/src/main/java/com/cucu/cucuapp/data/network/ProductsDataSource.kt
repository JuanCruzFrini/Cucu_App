package com.cucu.cucuapp.data.network

import com.cucu.cucuapp.application.Constants
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.cart.Cart
import com.cucu.cucuapp.data.models.cart.CartProduct
import com.cucu.cucuapp.data.models.items.ItemCategory
import com.cucu.cucuapp.data.models.items.ItemSaved
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.models.purchase.PurchaseReference
import com.cucu.cucuapp.data.models.purchase.PurchaseState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsDataSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val user:FirebaseUser?
) {

    suspend fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()

        val fetch = db.collection(Constants.PRODUCTS_COLL).get().await()

        fetch.documents.forEach{ document ->
            val product = document.toObject(Product::class.java)
            product?.id = document.id
            product?.let { products.add(it) }
        }

        return products
    }

    suspend fun getCategories(): List<ItemCategory> {
        val categories = mutableListOf<ItemCategory>()

        val fetch = db.collection(Constants.CATEGORIES_COLL)
            .orderBy(Constants.CATEGORY, Query.Direction.ASCENDING)
            .get()
            .await()

        fetch.documents.forEach{ document ->
            val category = document.toObject(ItemCategory::class.java)
            category?.let { categories.add(it) }
        }

        return categories
    }

    suspend fun getProductsByCategory(category:String):List<Product>{
        val products = mutableListOf<Product>()

        val fetch = db.collection(Constants.PRODUCTS_COLL)
            .whereEqualTo("category.category", category)
            .get()
            .await()

        fetch.documents.forEach { document ->
            val product = document.toObject(Product::class.java)
            product?.id = document.id
            product?.let { products.add(it) }
        }

        return products
    }

    suspend fun getDiscounts(): List<Product> {
        val products = mutableListOf<Product>()

        val fetch = db.collection(Constants.PRODUCTS_COLL).get().await()

        fetch.documents.forEach{ document ->
            val product = document.toObject(Product::class.java)
            product?.id = document.id

            if (product != null && product.newPrice!! < product.oldPrice!!){
                product.let { products.add(it) }
            }
        }

        return products
    }
     suspend fun getProductById(productId: String): Product {
        var product = Product()

         val document = db.collection(Constants.PRODUCTS_COLL).document(productId).get().await()

         val productObject = document.toObject(Product::class.java)
         productObject?.let {
             product = it
             product.id = document.id
         }

         return product
    }

    suspend fun saveInUsersHistory(productId:String) {
        user?.let {
            val historyRef = db.collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.HISTORY_COLL)

            val productWithThisId = historyRef
                .whereEqualTo(Constants.PRODUCT_ID, productId)
                .get().await()

            CoroutineScope(Dispatchers.IO).launch {
                if (productWithThisId.documents.isEmpty() || productWithThisId == null) {
                    async {
                        historyRef.add(ItemSaved(productId = productId)).await()
                    }.await()
                } else {
                    async {
                        productWithThisId.documents.forEach {
                            historyRef.document(it.id).delete()
                        }
                    }.await()
                    historyRef.add(ItemSaved(productId = productId)).await()
                }
            }
        }
    }

    suspend fun getUserHistory(): List<Product> {
        val products = mutableListOf<Product>()

        user?.let { user ->
            val fetch = db
                .collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.HISTORY_COLL)
                .orderBy(Constants.DATE, Query.Direction.DESCENDING)
                .get().await()

            fetch.documents.forEach { document ->
                val itemSaved = document.toObject(ItemSaved::class.java)
                itemSaved?.documentId = document.id
                val product = itemSaved?.productId?.let { getProductById(it) }
                product?.let { products.add(it) }
            }
        }

        return products.distinct()
    }

    suspend fun setFav(productId: String) : Boolean {
        var existInList = false

        user?.let { user ->
            val favoritesRef = db.collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.FAVORITES_COLL)

            val productWithThisId = favoritesRef
                .whereEqualTo(Constants.PRODUCT_ID, productId)
                .get().await()


            if (productWithThisId.isEmpty) {
                favoritesRef.add(ItemSaved(productId = productId)).await()
                existInList = true
            } else {
                productWithThisId.documents.forEach {
                    favoritesRef.document(it.id).delete().await()
                    existInList = false
                }
            }
        }
        return existInList
    }
    suspend fun getFavorites(): List<Product> {
        val products = mutableListOf<Product>()

        user?.let { user ->
            val fetch = db
                .collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.FAVORITES_COLL)
                .orderBy(Constants.DATE, Query.Direction.DESCENDING)
                .get().await()

            fetch.documents.forEach { document ->
                val itemSaved = document.toObject(ItemSaved::class.java)
                itemSaved?.documentId = document.id
                val product = itemSaved?.productId?.let { getProductById(it) }
                product?.let { products.add(it) }
            }
        }

        return products.distinct()
    }

    suspend fun checkIfExistInFavList(productId: String) : Boolean {
        var existInList = false

        user?.let { user ->
            val favoritesRef = db.collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.FAVORITES_COLL)

            val productWithThisId = favoritesRef
                .whereEqualTo(Constants.PRODUCT_ID, productId)
                .get().await()

            existInList = !productWithThisId.isEmpty
        }

        return existInList
    }
    fun createPurchase(purchase: Purchase) {
        user?.let { user ->
            db.collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.PURCHASES_COLL)
                .add(purchase).addOnSuccessListener { document ->
                    updateStock(purchase)

                    db.collection(Constants.PURCHASES_REFS_COLL)
                        .add(
                            PurchaseReference(
                                documentId = document.id,
                                uid = user.uid
                            )
                        )
                }
        }
    }
    suspend fun getPurchasesReferences(): List<Purchase> {
        val purchases = mutableListOf<Purchase>()

        val fetch = db.collection(Constants.PURCHASES_REFS_COLL).get().await()

        fetch.documents.forEach { document ->
            val purchaseRef = document.toObject(PurchaseReference::class.java)
            purchaseRef?.let { ref ->
                if (ref.uid != null && ref.documentId != null) {
                    val purchaseDoc = db.collection(Constants.USERS_COLL)
                        .document(ref.uid)
                        .collection(Constants.PURCHASES_COLL)
                        .document(ref.documentId)
                        .get().await()

                    val purchase = purchaseDoc.toObject(Purchase::class.java)
                    purchase?.id = ref.documentId
                    purchase?.let { purchases.add(it) }
                }

            }
        }

        return purchases.sortedBy { it.date }
    }

    suspend fun cancelPurchase(purchase: Purchase){
        user?.let { user ->
            purchase.id?.let { id ->
                db.collection(Constants.USERS_COLL)
                    .document(user.uid)
                    .collection(Constants.PURCHASES_COLL)
                    .document(id)
                    .update("state", PurchaseState.Cancelled().description)
                    .await()

                reloadStock(purchase)
            }
        }
    }

    private fun reloadStock(purchase: Purchase) {
        purchase.products?.forEach { cartProduct ->
            cartProduct.productId?.let { id ->

                CoroutineScope(Dispatchers.IO).launch {
                    var newStock:Int? = 0

                    async {
                        val product = getProductById(id)
                        newStock = product.stock?.plus(cartProduct.quantity!!)
                    }.await()

                    db.collection(Constants.PRODUCTS_COLL).document(id)
                        .update("stock", newStock).await()
                }
            }
        }
    }

    private fun updateStock(purchase: Purchase) {
        purchase.products?.forEach { cartProduct ->
            cartProduct.product.id?.let { id ->

                CoroutineScope(Dispatchers.IO).launch {
                    var newStock:Int? = 0

                    async {
                        val product = getProductById(id)
                        newStock = product.stock?.minus(cartProduct.quantity!!)
                    }.await()

                    db.collection(Constants.PRODUCTS_COLL).document(id)
                        .update("stock", newStock).await()
                }
            }
        }
    }

    suspend fun getPurchases():List<Purchase> {
        val purchases = mutableListOf<Purchase>()

        user?.let { user ->
            val fetch = db
                .collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.PURCHASES_COLL)
                .orderBy(Constants.DATE, Query.Direction.DESCENDING)
                .get().await()

            fetch.documents.forEach { document ->
                val purchase = document.toObject(Purchase::class.java)
                purchase?.id = document.id

                purchase?.products?.forEach { cartProduct ->
                    cartProduct.product.id = cartProduct.productId
                }

                purchase?.let { purchases.add(it) }
            }
        }

        return purchases
    }

    suspend fun getCart(): Cart {
        val cartProducts = mutableListOf<CartProduct>()
        val cart = Cart()

        user?.let {
            val fetch = db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.CART_COLL).get().await()

            fetch.documents.forEach { document ->
                val cartProduct = document.toObject(CartProduct::class.java)
                cartProduct?.documentId = document.id
                val product = cartProduct?.productId?.let { getProductById(it) }
                product?.let { cartProduct.product = it }
                cartProduct?.let { cartProducts.add(it) }
            }
            cart.products = cartProducts
        }
        return cart
    }

    suspend fun addToCart(productId: String, quantity:Int){
        user?.let {
            val cartRef = db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.CART_COLL)

            val productWithThisId = cartRef
                .whereEqualTo(Constants.PRODUCT_ID, productId)
                .get().await()

            CoroutineScope(Dispatchers.IO).launch {
                if (productWithThisId.documents.isEmpty() || productWithThisId == null) {
                    async {
                        cartRef.add(
                            CartProduct(
                                productId = productId,
                                quantity = quantity)
                        ).await()
                    }.await()
                } else {
                    async {
                        productWithThisId.documents.forEach { document ->
                            val cartProduct = document.toObject(CartProduct::class.java)
                            val newQuant = cartProduct?.quantity?.plus(quantity)
                            cartRef.document(document.id).update(Constants.QUANTITY, newQuant).await()
                        }
                    }.await()
                }
            }
        }
    }

    suspend fun removeCartProduct(documentId: String) {
        user?.let {
            db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.CART_COLL)
                .document(documentId).delete().await()
        }
    }

    suspend fun editCartProductQuantity(documentId: String, newQuant:Int){
        user?.let {
            val cartRef = db.collection(Constants.USERS_COLL)
                .document(it.uid)
                .collection(Constants.CART_COLL)

            CoroutineScope(Dispatchers.IO).launch {
                if (newQuant == 0) {
                    removeCartProduct(documentId)
                } else {
                    async {
                        cartRef.document(documentId)
                            .update(Constants.QUANTITY, newQuant)
                            .await()
                    }.await()
                }
            }
        }
    }

    suspend fun buyCart(purchase: Purchase){
        user?.let {
            createPurchase(purchase)
            clearCart()
        }
    }

    suspend fun clearCart() {
        user?.let { user ->
            val fetch = db.collection(Constants.USERS_COLL)
                .document(user.uid)
                .collection(Constants.CART_COLL)

            fetch.get().addOnSuccessListener { task ->
                task.documents.forEach { document ->
                    fetch.document(document.id).delete()
                }
            }.await()
        }
    }
}