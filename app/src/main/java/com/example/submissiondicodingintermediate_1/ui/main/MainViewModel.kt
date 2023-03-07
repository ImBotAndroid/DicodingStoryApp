package com.example.submissiondicodingintermediate_1.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import com.example.submissiondicodingintermediate_1.data.response.ListStoryItem

class MainViewModel(private val repository: Repository): ViewModel() {
    fun getAllStory(bearer: String): LiveData<PagingData<ListStoryItem>> = repository.getAllStory(bearer).cachedIn(viewModelScope)
}