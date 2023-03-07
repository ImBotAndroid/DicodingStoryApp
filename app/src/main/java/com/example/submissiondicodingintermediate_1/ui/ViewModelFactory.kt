package com.example.submissiondicodingintermediate_1.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissiondicodingintermediate_1.ui.maps.MapsViewModel
import com.example.submissiondicodingintermediate_1.ui.uploadstory.UploadViewModel
import com.example.submissiondicodingintermediate_1.data.networking.Repository
import com.example.submissiondicodingintermediate_1.dependency_injection.Injection
import com.example.submissiondicodingintermediate_1.ui.login.LoginViewModel
import com.example.submissiondicodingintermediate_1.ui.main.MainViewModel
import com.example.submissiondicodingintermediate_1.ui.setting.SettingViewModel
import com.example.submissiondicodingintermediate_1.ui.signup.SignUpViewModel

class ViewModelFactory private constructor(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }
}