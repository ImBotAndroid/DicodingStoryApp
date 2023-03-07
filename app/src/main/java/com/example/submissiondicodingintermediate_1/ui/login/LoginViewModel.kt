package com.example.submissiondicodingintermediate_1.ui.login

import androidx.lifecycle.ViewModel
import com.example.submissiondicodingintermediate_1.data.networking.Repository

class LoginViewModel(private val repository: Repository): ViewModel() {
    fun loginUser(email: String, password: String) = repository.loginUser(email, password)

    fun isLoading() = repository.isLoading
    
    fun setMessage() = repository.setMessage

    fun getUser() = repository.getUser()
}
