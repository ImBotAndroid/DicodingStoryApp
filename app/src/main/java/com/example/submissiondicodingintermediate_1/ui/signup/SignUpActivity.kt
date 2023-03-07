package com.example.submissiondicodingintermediate_1.ui.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.submissiondicodingintermediate_1.R
import com.example.submissiondicodingintermediate_1.databinding.ActivitySignUpBinding
import com.example.submissiondicodingintermediate_1.ui.ViewModelFactory
import com.example.submissiondicodingintermediate_1.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpButton()
        setUpViewModel()
        setUpView()
    }

    @Suppress("DEPRECATION")
    private fun setUpView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        supportActionBar?.hide()
        binding.progressBarSignUp.visibility = View.GONE
    }

    private fun setUpViewModel() {
        signUpViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[SignUpViewModel::class.java]
    }

    private fun setUpButton() {
        binding.buttonCreateAccountFSignup.setOnClickListener(this)
        binding.buttonLoginFSignup.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val name = binding.nameEditTextFSignup.text.toString()
        val email = binding.emailEditTextFSignup.text.toString()
        val password = binding.passwordEditTextFSignup.text.toString()
        when (v?.id){
            R.id.button_create_account_f_signup -> {
                signUpViewModel.isLoading().observe(this){
                    showLoading(it)
                }
                when {
                    name.isEmpty() -> {
                        binding.nameEditTextLayoutFSignup.error =
                            resources.getString(R.string.name_empty_alert)
                    }
                    email.isEmpty() -> {
                        binding.emailEditTextLayoutFSignup.error =
                            resources.getString(R.string.email_empty_alert)
                    }
                    password.isEmpty() -> {
                        binding.passwordEditTextLayoutFSignup.error =
                            resources.getString(R.string.password_empty_alert)
                    }
                }
                signUpViewModel.signUp(name, email, password).observe(this){ signUp ->
                    if (!signUp.error){
                        showAlertDialogSuccess()
                    } else if (signUp.error) {
                        showAlertDialogError()
                    }
                }
                binding.progressBarSignUp.visibility = View.VISIBLE
            }
            R.id.button_login_f_signup -> {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }
        }
    }

    private fun showAlertDialogSuccess() {
        AlertDialog.Builder(this).apply {
            setMessage(R.string.success_crate_account)
            setPositiveButton(R.string.oke) { _, _ ->
                finish()
            }
            create()
            show()
        }
    }

    private fun showAlertDialogError() {
        val message: String
        AlertDialog.Builder(this).apply {
            signUpViewModel.setMessage().apply {
                message = value.toString()
            }
            if (message == "Email is already taken"){
                setMessage(R.string.email_already_taken)
                setPositiveButton(R.string.oke) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarSignUp.visibility = View.VISIBLE
        }else{
            binding.progressBarSignUp.visibility = View.GONE
        }
    }
}