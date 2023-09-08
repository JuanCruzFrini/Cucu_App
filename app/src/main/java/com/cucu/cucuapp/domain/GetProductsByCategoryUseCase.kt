package com.cucu.cucuapp.domain

import com.cucu.cucuapp.data.repository.ProductsRepository
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend fun getProductsByCategory(category:String) = repository.getProductsByCategory(category)
}