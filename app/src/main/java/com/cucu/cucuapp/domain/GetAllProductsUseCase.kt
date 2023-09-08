package com.cucu.cucuapp.domain

import com.cucu.cucuapp.data.repository.ProductsRepository
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke() = repository.getAllProducts()
}