package com.example.submissiondicodingintermediate_1.ui.signup

import androidx.lifecycle.ViewModel
import com.example.submissiondicodingintermediate_1.data.networking.Repository

class SignUpViewModel(private val repository: Repository): ViewModel() {
    fun signUp(name: String, email: String, password: String) = repository.registerNewUser(name, email, password)

    fun setMessage() = repository.setMessage

    fun isLoading() = repository.isLoading
}