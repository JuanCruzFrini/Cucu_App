package com.cucu.cucuapp.presentation.detail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cucu.MainDispatcherRule
import com.cucu.cucuapp.data.models.Product
import com.cucu.cucuapp.data.models.purchase.Purchase
import com.cucu.cucuapp.data.repository.ProductsRepository
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatchers = MainDispatcherRule()

    @RelaxedMockK
    lateinit var repository: ProductsRepository

    @RelaxedMockK
    lateinit var auth: FirebaseAuth

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DetailViewModel(auth, repository)
    }

    @Test
    fun `setFav success and exist in user list`() = runBlocking {
        val productId = "test id"
        val expected = true

        coEvery { repository.setFav(productId) } returns expected

        viewModel.setFav(productId)

        coVerify { repository.setFav(productId) }
        assert(viewModel.existInFavList.value == expected)
    }

    @Test
    fun `setFav success and does not exist in user list`() = runBlocking {
        val productId = "test id"
        val expected = false

        coEvery { repository.setFav(productId) } returns expected

        viewModel.setFav(productId)

        coVerify { repository.setFav(productId) }
        assert(viewModel.existInFavList.value == expected)
    }

    @Test
    fun `setFav error`() = runBlocking {
        val productId = "test id"
        val error = Exception("error message")

        coEvery { repository.setFav(productId) } throws error

        viewModel.setFav(productId)

        coVerify { repository.setFav(productId) }
        assert(viewModel.existInFavList.value == null)
    }

    @Test
    fun `checkIfExistInFavList success and exist in user list`() = runBlocking {
        val productId = "test id"
        val expected = true

        coEvery { repository.setFav(productId) } returns expected

        viewModel.setFav(productId)

        coVerify { repository.setFav(productId) }
        assert(viewModel.existInFavList.value == expected)
    }
    @Test
    fun `checkIfExistInFavList success and does not exist in user list`() = runBlocking {
        val productId = "test id"
        val expected = false

        coEvery { repository.setFav(productId) } returns expected

        viewModel.setFav(productId)

        coVerify { repository.setFav(productId) }
        assert(viewModel.existInFavList.value == expected)
    }

    @Test
    fun `checkIfExistInFavList error`() = runBlocking {
        val productId = "test id"
        val error = Exception("error message")

        coEvery { repository.checkIfExistInFavList(productId) } throws error

        viewModel.checkIfExistInFavList(productId)

        coVerify { repository.checkIfExistInFavList(productId) }
        assert(viewModel.existInFavList.value == null)
    }

    @Test
    fun `increaseSeen times should call repo increaseSeenTimes`() = runBlocking {
        val product = mockk<Product>()

        coEvery { repository.increaseSeenTimes(product) } just runs

        viewModel.increaseSeenTimes(product)

        coVerify { repository.increaseSeenTimes(product) }
    }
    @Test
    fun `saveInUserHistory times should call repo saveInUserHistory`() = runBlocking {
        val productId = "test id"

        coEvery { repository.saveInUsersHistory(productId) } just runs

        viewModel.saveInUserHistory(productId)

        coVerify { repository.saveInUsersHistory(productId) }
    }

    @Test
    fun `addToCart success`() = runBlocking {
        val productId = "test id"
        val quantity = 1

        coEvery { repository.addToCart(productId, quantity) } just runs

        viewModel.addToCart(productId, quantity)

        coVerify { repository.addToCart(productId, quantity) }
    }
    @Test
    fun `addToCart error`() = runBlocking {
        val productId = "test id"
        val quantity = 1
        val error = Exception("error message")

        coEvery { repository.addToCart(productId, quantity) } throws error

        viewModel.addToCart(productId, quantity)

        coVerify { repository.addToCart(productId, quantity) }
    }

    @Test
    fun `createPurchase success`() = runBlocking {
        val purchase = mockk<Purchase>()
        val expected = "purchase id"

        coEvery { repository.createPurchase(purchase) } returns expected

        viewModel.createPurchase(purchase)

        coVerify { repository.createPurchase(purchase) }
        assert(viewModel.purchaseId.value == expected)
    }


    @Test
    fun `createPurchase error`() = runBlocking {
        val purchase = mockk<Purchase>()
        val error = Exception("error message")

        coEvery { repository.createPurchase(purchase) } throws error

        viewModel.createPurchase(purchase)

        coVerify { repository.createPurchase(purchase) }
        assert(viewModel.purchaseId.value == null)
    }
}
