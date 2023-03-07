package com.example.submissiondicodingintermediate_1.ui.setting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {

    private lateinit var settingViewModel: SettingViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp(){
        settingViewModel = SettingViewModel(repository)
    }

    @Test
    fun `When logout should delete data in preferences`() {
        settingViewModel.deleteUser()
        Mockito.verify(repository).deleteUser()
    }
}