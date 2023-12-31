package com.cucu.cucuapp.data.repository

import com.cucu.cucuapp.data.fakedatasources.FakeDataSource
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.network.ProductsDataSource
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val dataSource: ProductsDataSource,
    private val fakeDataSource: FakeDataSource
) {
    suspend fun getAllProducts() = dataSource.getAllProducts()
    suspend fun getCategories() = fakeDataSource.categoriesList//dataSource.getCategories()
    suspend fun getDiscounts() = dataSource.getDiscounts()
    suspend fun getHistory() = dataSource.getUserHistory()
    suspend fun saveInUsersHistory(productId:String) = dataSource.saveInUsersHistory(productId)
    suspend fun setFav(productId:String) = dataSource.setFav(productId)
    suspend fun checkIfExistInFavList(productId:String) = dataSource.checkIfExistInFavList(productId)
    suspend fun getFavorites() = dataSource.getFavorites()
    suspend fun getPurchases() = dataSource.getPurchases()
    suspend fun getPurchaseById(purchaseId:String) = dataSource.getPurchaseById(purchaseId)
    suspend fun increaseSeenTimes(product: Product) = dataSource.increaseSeenTimes(product)
    suspend fun createPurchase(purchase: Purchase):String = dataSource.createPurchase(purchase)
    suspend fun cancelPurchase(purchase: Purchase) = dataSource.cancelPurchase(purchase)
    suspend fun getCart() = dataSource.getCart()
    suspend fun addToCart(productId: String, quantity:Int) = dataSource.addToCart(productId, quantity)
    suspend fun editCartProductQuantity(documentId: String, quantity:Int) = dataSource.editCartProductQuantity(documentId, quantity)
    suspend fun clearCart() = dataSource.clearCart()
    suspend fun buyCart(purchase: Purchase) = dataSource.buyCart(purchase)
    suspend fun removeCartProduct(documentId: String) = dataSource.removeCartProduct(documentId)
    suspend fun getProductsByCategory(category: String) = dataSource.getProductsByCategory(category)
}