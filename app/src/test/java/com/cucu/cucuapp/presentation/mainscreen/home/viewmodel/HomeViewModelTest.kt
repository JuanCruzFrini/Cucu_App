package com.cucu.cucuapp.presentation.mainscreen.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cucu.MainDispatcherRule
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.domain.GetAllProductsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HomeViewModelTest{

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatchers = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var getAllProductsUseCase: GetAllProductsUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        viewModel = HomeViewModel(getAllProductsUseCase)
    }

    @Test
    fun `get all products success`() = runBlocking  {
        val expected = listOf<Product>()
        coEvery { getAllProductsUseCase.invoke() } returns expected

        viewModel.getAllProducts()

        coVerify { getAllProductsUseCase.invoke() }
        assert( viewModel.productsList.value == expected )
    }

    @Test
    fun `get all products error`(){
        val error = "Error"
        coEvery { getAllProductsUseCase.invoke() } throws Exception(error)

        viewModel.getAllProducts()

        coVerify { getAllProductsUseCase.invoke() }
        assert(viewModel.productsList.value == null)
    }
}