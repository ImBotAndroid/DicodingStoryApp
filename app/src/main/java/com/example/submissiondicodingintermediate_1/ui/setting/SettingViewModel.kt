package com.example.submissiondicodingintermediate_1.ui.setting

import androidx.lifecycle.ViewModel
import com.example.submissiondicodingintermediate_1.data.networking.Repository

class SettingViewModel(private val repository: Repository): ViewModel() {
    fun deleteUser() = repository.deleteUser()
}