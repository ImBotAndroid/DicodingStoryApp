package com.example.submissiondicodingintermediate_1.ui.uploadstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import com.example.submissiondicodingintermediate_1.data.response.UploadStoriesResponse
import com.example.submissiondicodingintermediate_1.ui.utils.DataDummy
import com.example.submissiondicodingintermediate_1.ui.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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
class UploadViewModelTest {

    private lateinit var uploadViewModel: UploadViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp(){
        uploadViewModel = UploadViewModel(repository)
    }

    @Test
    fun `When upload story should get return response`() = runTest {
        val bearer = "a"
        val file = MultipartBody.Part.createFormData("photo", "")
        val desc = "Tes".toRequestBody()

        val dummyStoryUpload = DataDummy.dummyUploadStory()
        val expectedUpload = MutableLiveData<UploadStoriesResponse>()

        expectedUpload.value = dummyStoryUpload
        Mockito.`when`(repository.uploadStory(bearer, file, desc)).thenReturn(expectedUpload)

        val actualUpload = uploadViewModel.uploadStory(bearer, file, desc).getOrAwaitValue()

        Assert.assertNotNull(actualUpload)
        Assert.assertEquals(expectedUpload.value, actualUpload)
    }
}