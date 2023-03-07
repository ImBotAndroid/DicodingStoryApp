package com.example.submissiondicodingintermediate_1.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import com.example.submissiondicodingintermediate_1.data.response.SignUpResponse
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
class SignUpViewModelTest {

    private lateinit var signUpViewModel: SignUpViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp(){
        signUpViewModel = SignUpViewModel(repository)
    }

    @Test
    fun `When signup success should return response`() = runTest {
        val name = "name"
        val email = "email"
        val password = "password"

        val dummyResponseSuccess = DataDummy.dummyUserRegisterSuccess()
        val expectedResponseSuccess = MutableLiveData<SignUpResponse>()

        expectedResponseSuccess.value = dummyResponseSuccess
        Mockito.`when`(repository.registerNewUser(name, email, password)).thenReturn(expectedResponseSuccess)

        val actualResponseSuccess = signUpViewModel.signUp(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).registerNewUser(name, email, password)

        Assert.assertNotNull(actualResponseSuccess)
        Assert.assertEquals(expectedResponseSuccess.value, actualResponseSuccess)
    }

    @Test
    fun `When signup error should return response`() = runTest{
        val name = "name"
        val email = "email"
        val password = "password"

        val dummyResponseFailed = DataDummy.dummyUserRegisterFailed()
        val expectedResponseFailed = MutableLiveData<SignUpResponse>()

        expectedResponseFailed.value = dummyResponseFailed
        Mockito.`when`(repository.registerNewUser(name, email, password)).thenReturn(expectedResponseFailed)

        val actualResponseFailed = signUpViewModel.signUp(name, email, password).getOrAwaitValue()
        Mockito.verify(repository).registerNewUser(name, email, password)

        Assert.assertNotNull(actualResponseFailed)
        Assert.assertEquals(expectedResponseFailed.value, actualResponseFailed)
    }

    @Test
    fun `when signup error should show message in alert dialog`() = runTest {
        val dummyMessage = DataDummy.dummyErrorMessage()
        val expectedMessage = MutableLiveData<String>()

        expectedMessage.value = dummyMessage
        Mockito.`when`(repository.setMessage).thenReturn(expectedMessage)

        val actualMessage = signUpViewModel.setMessage().getOrAwaitValue()
        Mockito.verify(repository).setMessage

        Assert.assertEquals(expectedMessage.value, actualMessage)
    }

    @Test
    fun `when signup should show loading (progress bar value = true)`() = runTest {
        val dummyLoading = DataDummy.dummyLoading()
        val expectedLoading = MutableLiveData<Boolean>()

        expectedLoading.value = dummyLoading
        Mockito.`when`(repository.isLoading).thenReturn(expectedLoading)

        val actualLoading = signUpViewModel.isLoading().getOrAwaitValue()
        Mockito.verify(repository).isLoading

        Assert.assertEquals(expectedLoading.value, actualLoading)
    }
}