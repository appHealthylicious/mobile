package com.dicoding.picodiploma.loginwithanimation.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.extrainfo.ExtraInfoActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private var loginObserver: Observer<LoginResponse?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.buttonLogin.setOnClickListener {

            //loading UI view setting
            binding.scrollViewSignIn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE


            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.resetLoginResult()
            viewModel.login(email, password)

            loginObserver?.let { viewModel.loginResult.removeObserver(it) }
            loginObserver = Observer { loginResponse ->
                if (loginResponse != null) {
                    println("Login response is not null.")
                    if (loginResponse?.message == "Login successful") {
                        println("Success")
                        showSuccessDialog()
                    } else {

                        binding.scrollViewSignIn.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        showFailDialog(loginResponse.message ?: "gagal login")
                    }
                }
            }
            viewModel.loginResult.observe(this, loginObserver!!)



        }
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("You have successfully logged in.")
            setPositiveButton("Continue") { _, _ ->
                val intent = Intent(context, ExtraInfoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }


    private fun showFailDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Oh no!")
            setMessage("${message}. Please try again!")
            setPositiveButton("Ok") { _, _ ->
                //TODO
            }
            create()
            show()
        }
    }

}