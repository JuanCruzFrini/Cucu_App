package com.cucu.cucuapp.data.repository

import com.cucu.cucuapp.data.fakedatasources.FakeDataSource
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    private val fakeDataSource: FakeDataSource
) {

    suspend fun getAllProducts() = fakeDataSource.productsFakeList
    suspend fun getHistory() = fakeDataSource.productsFakeList
    suspend fun getFavorites() = fakeDataSource.productsFakeList

    suspend fun getCart() = fakeDataSource.fakeCart
    suspend fun getPurchases() = fakeDataSource.purchasesFakeList
    suspend fun getCategories() = fakeDataSource.categoriesList

    suspend fun getProductsByCategory(category: String) = fakeDataSource.productsByCategory(category)
}