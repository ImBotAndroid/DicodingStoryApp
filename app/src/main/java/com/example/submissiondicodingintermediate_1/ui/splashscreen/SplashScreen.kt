package com.example.submissiondicodingintermediate_1.ui.splashscreen

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.submissiondicodingintermediate_1.databinding.ActivitySplashBinding
import com.example.submissiondicodingintermediate_1.ui.ViewModelFactory
import com.example.submissiondicodingintermediate_1.ui.login.LoginActivity
import com.example.submissiondicodingintermediate_1.ui.login.LoginViewModel
import com.example.submissiondicodingintermediate_1.ui.main.MainActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpViewModel()
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.hide()
    }

    @Suppress("DEPRECATION")
    private fun setUpViewModel() {
        val splashViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]

        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.getUser().observe(this){ user ->
                if (user.token?.isEmpty() == true){
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }, 1500)
    }
}