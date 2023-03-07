package com.example.submissiondicodingintermediate_1.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import com.example.submissiondicodingintermediate_1.data.response.ListStoryItem
import com.example.submissiondicodingintermediate_1.ui.main.StoryPagingSource
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
class MapsViewModelTest {

    private lateinit var mapsViewModel: MapsViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: Repository

    @Before
    fun setUp(){
        mapsViewModel = MapsViewModel(repository)
    }

    @Test
    fun `when Get Location Should Not Null and Return Data`() = runTest {
        val bearer = "Token"
        val dummyStoryLocation = DataDummy.dummyListStory()
        val expectedStoryLocation = MutableLiveData<List<ListStoryItem>>()

        expectedStoryLocation.value = dummyStoryLocation
        Mockito.`when`(repository.storyLocation(bearer)).thenReturn(expectedStoryLocation)

        val actualStoryLocation = mapsViewModel.storyLocation(bearer).getOrAwaitValue()

        Assert.assertNotNull(actualStoryLocation)
        Assert.assertEquals(expectedStoryLocation.value, actualStoryLocation)
        Assert.assertEquals(expectedStoryLocation.value?.size, actualStoryLocation.size)
    }
}