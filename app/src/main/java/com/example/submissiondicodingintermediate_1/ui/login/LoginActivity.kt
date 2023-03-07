package com.example.submissiondicodingintermediate_1.ui.login

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.submissiondicodingintermediate_1.*
import com.example.submissiondicodingintermediate_1.databinding.ActivityLoginBinding
import com.example.submissiondicodingintermediate_1.ui.ViewModelFactory
import com.example.submissiondicodingintermediate_1.ui.main.MainActivity
import com.example.submissiondicodingintermediate_1.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpButton()
        setUpViewModel()
        setUpView()
        setUpAnimation()
        isInternetAvailable()
    }

    private fun isInternetAvailable() {
        if (!checkConnection()){
            showAlertDialog()
        }
    }

    private fun checkConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val connection = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            return when (connection != null) {
                connection?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                connection?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                connection?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        return false
    }

    private fun setUpAnimation() {
        ObjectAnimator.ofFloat(binding.ivImageFLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = animDuration.toLong()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.hide()
        binding.progressBarLogin.visibility = View.GONE
    }

    private fun setUpViewModel() {
        loginViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]
    }

    private fun setUpButton() {
        binding.buttonLoginFLogin.setOnClickListener(this)
        binding.buttonSignupFLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_login_f_login -> {
                loginViewModel.isLoading().observe(this){
                    showLoading(it)
                }
                val email = binding.editTextEmailFLogin.text.toString()
                val password = binding.editTextPasswordFLogin.text.toString()
                when {
                    email.isEmpty() -> {
                        binding.emailEditTextLayoutFLogin.error = resources.getString(R.string.email_empty_alert)
                    }
                    password.isEmpty() -> {
                        binding.passwordEditTextLayoutFLogin.error = resources.getString(R.string.password_empty_alert)
                    }
                }
                loginViewModel.loginUser(email, password).observe(this){ login ->
                    if (!login.error){
                        Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else if (login.error) {
                        showAlertDialog()
                    }
                }
            }

            R.id.button_signup_f_login -> {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
        }
    }

    private fun showAlertDialog() {
        val message: String
        AlertDialog.Builder(this).apply {
            loginViewModel.setMessage().apply {
                message = value.toString()
            }
            when (message){
                "User not found" -> {
                    setMessage(R.string.user_not_found)
                }
                "Invalid password"  -> {
                    setMessage(R.string.wrong_password)
                }
            }
            setPositiveButton(R.string.oke) { dialog, _, ->
                dialog.dismiss()
            }
            if (!checkConnection()){
                setMessage(R.string.no_internet)
                setPositiveButton(R.string.menu_setting) {_, _, ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                setNegativeButton(R.string.cancel) { dialog, _, ->
                    dialog.dismiss()}
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
        }else{
            binding.progressBarLogin.visibility = View.GONE
        }
    }

    companion object {
        private const val animDuration = 6000
    }
}