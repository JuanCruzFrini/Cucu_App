package com.cucu.cucuapp.presentation.mainscreen.discounts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cucu.MainDispatcherRule
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.repository.ProductsRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DiscountsViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatchers = MainDispatcherRule()

    @RelaxedMockK
    lateinit var repository: ProductsRepository
    private lateinit var viewModel: DiscountsViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        viewModel = DiscountsViewModel(repository)
    }

    @Test
    fun `getDiscounts success`() = runBlocking {
        val expected = mockk<List<Product>>()

        coEvery { repository.getDiscounts() } returns expected

        viewModel.getDiscounts()

        coVerify { repository.getDiscounts() }
        assert(viewModel.discountsList.value == expected)
    }

    @Test
    fun `getDiscounts error`() = runBlocking {
        val error = Exception("error message")

        coEvery { repository.getDiscounts() } throws error

        viewModel.getDiscounts()

        coVerify { repository.getDiscounts() }
        assert(viewModel.discountsList.value == null)
    }
}