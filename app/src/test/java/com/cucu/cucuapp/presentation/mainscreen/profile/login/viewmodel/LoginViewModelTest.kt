package com.cucu.cucuapp.presentation.mainscreen.profile.login.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cucu.MainDispatcherRule
import com.cucu.cucuapp.data.models.User
import com.cucu.cucuapp.data.repository.AuthRepository
import com.cucu.cucuapp.data.repository.NotificationsRepository
import com.google.firebase.auth.AuthCredential
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

class LoginViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatchers = MainDispatcherRule()

    @RelaxedMockK
    lateinit var auth: FirebaseAuth

    @RelaxedMockK
    lateinit var notificationsRepo: NotificationsRepository

    @RelaxedMockK
    lateinit var authRepo: AuthRepository

    private lateinit var viewModel: LoginViewModel
    @Before
    fun setUp(){
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(auth, authRepo, notificationsRepo)
    }

    @Test
    fun `getNotis success`() = runBlocking {
        val expected = 4
        coEvery { notificationsRepo.getNotificationsNotOpen() } returns expected

        viewModel.getNotificationsNotOpenQuant()

        coVerify { notificationsRepo.getNotificationsNotOpen() }
        assert(viewModel.notOpenedNotifications.value == expected)
    }

    @Test
    fun `getNotis error`() = runBlocking {
        val error = Exception("error message")
        coEvery { notificationsRepo.getNotificationsNotOpen() } throws error

        viewModel.getNotificationsNotOpenQuant()

        coVerify { notificationsRepo.getNotificationsNotOpen() }
        assert(viewModel.notOpenedNotifications.value == null)
    }

    @Test
    fun `signIn success`() = runBlocking {
        val credential = mockk<AuthCredential>()
        val user = mockk<User>()
        coEvery { authRepo.signIn(credential) } returns user

        viewModel.signIn(credential)

        coVerify { authRepo.signIn(credential) }
        assert(viewModel.user.value == user)
    }

    @Test
    fun `signIn error`() = runBlocking {
        val credential = mockk<AuthCredential>()
        val error = Exception("error message")
        coEvery { authRepo.signIn(credential) } throws error

        viewModel.signIn(credential)

        coVerify { authRepo.signIn(credential) }
        assert(viewModel.user.value == null)
    }

    @Test
    fun `signOut should call repo signOut`() = runBlocking {
        coEvery { authRepo.signOut() } just runs
        viewModel.signOut()
        coVerify { authRepo.signOut() }
    }
}