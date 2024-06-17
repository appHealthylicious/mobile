package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding
    private var signupObserver: Observer<RegisterResponse?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        emailFocusListener()
        confirmPasswordListener()
        passwordListener()
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

    private fun emailFocusListener() {
        binding.editTextEmail.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.editTextEmailLayout.helperText = validEmail()
            }
        }
    }

    private fun confirmPasswordListener() {
        binding.editTextConfirmPassword.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.editTextConfirmPasswordLayout.helperText = validConfirmPassword()
            }
        }
    }

    private fun passwordListener(){
        binding.edtTextPassword.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.editTextPasswordLayout.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.edtTextPassword.text.toString()
        if(passwordText.length < 6)
        {
            return "Password must be 6 characters or longer"
        }
        return null
    }

    private fun validConfirmPassword(): String? {
        val confirmPasswordText = binding.edtTextPassword.text.toString()
        val passwordText = binding.editTextConfirmPassword.text.toString()
        if(passwordText != confirmPasswordText)
        {
            return "Passwords do not match"
        }
        return null
    }

    private fun validEmail(): String?
    {
        val emailText = binding.editTextEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }

        return null

    }

    private fun setupAction() {
        binding.buttonRegister.setOnClickListener {

            //loading UI view setting
            binding.scViewSignUp.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val email = binding.editTextEmail.text.toString()
            val password = binding.edtTextPassword.text.toString()

            viewModel.resetRegister()
            signupObserver?.let { viewModel.result.removeObserver(it) }

            signupObserver = Observer { registerResponse ->
                if(binding.editTextConfirmPasswordLayout.helperText == "Passwords do not match"){
                    showFailDialog("Passwords do not match!")
                    binding.scViewSignUp.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                } else {
                    if (registerResponse != null){
                        if (registerResponse.message == "User registered successfully"){
                            showSuccessDialog(email)
                        } else {

                            binding.scViewSignUp.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            showFailDialog(registerResponse.message ?: "gagal register")
                        }
                    }
                }
            }
            viewModel.result.observe(this, signupObserver!!)

            viewModel.register(email, password)

        }
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun showSuccessDialog(email: String){
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("Akun dengan $email sudah jadi nih. Yuk, diet.")
            setPositiveButton("Lanjut") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            create()
            show()
        }
    }

    private fun showFailDialog(message: String){
        AlertDialog.Builder(this).apply {
            setTitle("Oh tidak!")
            setMessage("Pembuatan akun gagal. Error: $message.")
            setPositiveButton("Oke") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}