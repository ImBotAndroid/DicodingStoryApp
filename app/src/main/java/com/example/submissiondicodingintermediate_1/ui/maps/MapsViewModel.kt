package com.example.submissiondicodingintermediate_1.ui.maps

import androidx.lifecycle.ViewModel
import com.example.submissiondicodingintermediate_1.data.networking.Repository

class MapsViewModel(private val repository: Repository): ViewModel() {
    fun storyLocation(bearer: String) = repository.storyLocation(bearer)
}