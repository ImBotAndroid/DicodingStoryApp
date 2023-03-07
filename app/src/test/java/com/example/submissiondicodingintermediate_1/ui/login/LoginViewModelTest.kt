package com.example.submissiondicodingintermediate_1.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import com.example.submissiondicodingintermediate_1.data.response.LoginResponse
import com.example.submissiondicodingintermediate_1.data.response.LoginResult
import com.example.submissiondicodingintermediate_1.ui.utils.DataDummy
import com.example.submissiondicodingintermediate_1.ui.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp(){
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `When login should get result and return response success`() = runTest {

        val email = "email"
        val password = "password"

        val dummyUserData = DataDummy.dummyLoginSuccess()
        val expectedUserData = MutableLiveData<LoginResponse>()

        expectedUserData.value = dummyUserData
        Mockito.`when`(repository.loginUser(email, password)).thenReturn(expectedUserData)

        val actualUserData = loginViewModel.loginUser(email, password).getOrAwaitValue()

        Assert.assertNotNull(actualUserData)
        Assert.assertEquals(expectedUserData.value, actualUserData)
    }

    @Test
    fun `When login should get result and return response failed`() = runTest {
        val email = "email"
        val password = "password"

        val dummyUserData = DataDummy.dummyLoginFailed()
        val expectedUserData = MutableLiveData<LoginResponse>()

        expectedUserData.value = dummyUserData
        Mockito.`when`(repository.loginUser(email, password)).thenReturn(expectedUserData)

        val actualUserData = loginViewModel.loginUser(email, password).getOrAwaitValue()
        Mockito.verify(repository).loginUser(email, password)

        Assert.assertNotNull(actualUserData)
        Assert.assertEquals(expectedUserData.value, actualUserData)
    }

    @Test
    fun `when login should show loading (progress bar value = true)`() = runTest {
        val dummyLoading = DataDummy.dummyLoading()
        val expectedLoading = MutableLiveData<Boolean>()

        expectedLoading.value = dummyLoading
        Mockito.`when`(repository.isLoading).thenReturn(expectedLoading)

        val actualLoading = loginViewModel.isLoading().getOrAwaitValue()
        Mockito.verify(repository).isLoading

        Assert.assertEquals(expectedLoading.value, actualLoading)
    }

    @Test
    fun `when login error for password or email should show message in alert dialog`() = runTest {
        val dummyMessage = DataDummy.dummyErrorMessage()
        val expectedMessage = MutableLiveData<String>()

        expectedMessage.value = dummyMessage
        Mockito.`when`(repository.setMessage).thenReturn(expectedMessage)

        val actualMessage = loginViewModel.setMessage().getOrAwaitValue()
        Mockito.verify(repository).setMessage

        Assert.assertEquals(expectedMessage.value, actualMessage)
    }

    @Test
    fun `when login success get user data to preference`() = runTest {
        val dummyDataUser = DataDummy.dummyUserData()
        val expectedDataUser = MutableLiveData<LoginResult>()

        expectedDataUser.value = dummyDataUser
        Mockito.`when`(repository.getUser()).thenReturn(expectedDataUser)

        val actualDataUser = loginViewModel.getUser().getOrAwaitValue()
        Mockito.verify(repository).getUser()

        Assert.assertEquals(expectedDataUser.value, actualDataUser)
    }
}