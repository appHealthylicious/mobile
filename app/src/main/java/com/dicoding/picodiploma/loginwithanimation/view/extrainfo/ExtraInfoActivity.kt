package com.dicoding.picodiploma.loginwithanimation.view.extrainfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.data.response.UpdateResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityExtraInfoBinding
import com.dicoding.picodiploma.loginwithanimation.view.ExtraModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class ExtraInfoActivity : AppCompatActivity() {
    private val viewModel by viewModels<ExtraViewModel> {
        ExtraModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityExtraInfoBinding
    private var updateObserver: Observer<UpdateResponse?>? = null
    private var skip: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideUI()
        if(skip.isEmpty()){
            println("Is empty")
            viewModel.getUid().observe(this, Observer { uid ->
                println("Check UID: $uid")
                if (uid.isNotEmpty()) {
                    viewModel.getUserProfile(uid)
                    observeProfileResult()
                }
                skip = "NO"
            })
        }

        hideUI()
        setupAction()
    }

    private fun hideUI(){
        binding.viewExtra.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showUI(){
        binding.viewExtra.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun observeProfileResult() {
        viewModel.profileResult.observe(this, Observer { profileResponse ->
            if (profileResponse?.userProfile?.username != "" && skip != "NO")  {
                // Profile result is not null
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                showUI()
            }
        })
    }
    private fun setupAction() {
        binding.buttonSubmit.setOnClickListener {

            //loading UI view setting
            hideUI()

            val username = binding.editTextUsername.text.toString()
            val age = binding.editTextAge.text.toString()
            val weight = binding.editTextWeight.text.toString()
            val height = binding.editTextHeight.text.toString()

            viewModel.resetResult()
            viewModel.updateUserProfile(username, age, weight, height)

            updateObserver?.let { viewModel.updateResult.removeObserver(it) }
            updateObserver = Observer { updateResponse ->
                if (updateResponse != null) {
                    println("Update response is not null.")
                    if (updateResponse?.message == "User profile updated successfully") {
                        println("Success")
                        showSuccessDialog()
                    } else {
                        showUI()
                        showFailDialog(updateResponse.message ?: "gagal login")
                    }
                }
            }
            viewModel.updateResult.observe(this, updateObserver!!)



        }
    }
    private fun showSuccessDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("Data submitted successfully.")
            setPositiveButton("Continue") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
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
            setPositiveButton("Oke bro") { _, _ ->
                //TODO
            }
            create()
            show()
        }
    }
}