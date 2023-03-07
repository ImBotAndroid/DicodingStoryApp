package com.example.submissiondicodingintermediate_1.dependency_injection

import android.content.Context
import com.example.submissiondicodingintermediate_1.data.local.UserPreference
import com.example.submissiondicodingintermediate_1.data.api.ApiConfig
import com.example.submissiondicodingintermediate_1.data.networking.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context)
        return Repository.getInstance(apiService, userPreference)
    }
}