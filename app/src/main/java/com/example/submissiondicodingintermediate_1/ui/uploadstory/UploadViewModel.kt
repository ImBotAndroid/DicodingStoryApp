package com.example.submissiondicodingintermediate_1.ui.uploadstory

import androidx.lifecycle.ViewModel
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadViewModel(private val repository: Repository): ViewModel() {
    fun uploadStory(bearer: String, file: MultipartBody.Part, description: RequestBody) = repository.uploadStory(bearer, file, description)
    fun isLoading() = repository.isLoading
}