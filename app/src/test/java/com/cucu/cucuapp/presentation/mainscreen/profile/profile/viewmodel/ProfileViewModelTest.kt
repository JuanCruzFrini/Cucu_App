package com.cucu.cucuapp.presentation.mainscreen.profile.profile.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cucu.MainDispatcherRule
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.domain.GetFavoritesUseCase
import com.cucu.cucuapp.domain.GetHistoryUseCase
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatchers = MainDispatcherRule()

    @RelaxedMockK
    lateinit var getFavoritesUseCase: GetFavoritesUseCase
    @RelaxedMockK
    lateinit var getHistoryUseCase: GetHistoryUseCase
    @RelaxedMockK
    lateinit var auth: FirebaseAuth

    lateinit var viewModel: ProfileViewModel
    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        viewModel = ProfileViewModel(getFavoritesUseCase, getHistoryUseCase, auth)
    }

    @Test
    fun `get favorites success`() = runBlocking {
        val expected = mockk<List<Product>>()
        val observer = mockk<Observer<Boolean>>(relaxed = true)

        coEvery { getFavoritesUseCase.invoke() } returns expected

        viewModel.loadingFavorites.observeForever(observer)

        viewModel.getFavorites()

        coVerify { getFavoritesUseCase.invoke() }
        verifyOrder {
            observer.onChanged(true)
            observer.onChanged(false)
        }
        assert(viewModel.favorites.value == expected)
    }

    @Test
    fun `get favorites error`() = runBlocking {
        val error = Exception("error message")
        val observer = mockk<Observer<Boolean>>(relaxed = true)

        coEvery { getFavoritesUseCase.invoke() } throws error

        viewModel.loadingFavorites.observeForever(observer)

        viewModel.getFavorites()

        coVerify { getFavoritesUseCase.invoke() }
        verifyOrder {
            observer.onChanged(true)
            observer.onChanged(false)
        }
        assert(viewModel.favorites.value == null)
    }
}