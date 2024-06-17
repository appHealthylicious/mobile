package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val emailTextView: TextView = view.findViewById(R.id.profile_email_value)
        val usernameTextView: TextView = view.findViewById(R.id.profile_username_value)
        val ageTextView: TextView = view.findViewById(R.id.profile_age_value)
        val weightTextView: TextView = view.findViewById(R.id.profile_weight_value)
        val heightTextView: TextView = view.findViewById(R.id.profile_height_value)
        val logoutButton: Button = view.findViewById(R.id.button_logout)
        val resetButton: Button = view.findViewById(R.id.button_clear)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            emailTextView.text = user.email
            usernameTextView.text = user.extraInfo.username
            ageTextView.text = user.extraInfo.age
            weightTextView.text = user.extraInfo.weight + " kg"
            heightTextView.text = user.extraInfo.height + " cm"
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
        }

        resetButton.setOnClickListener {
            viewModel.getReset()
            Toast.makeText(requireContext(), "Reset local data successful", Toast.LENGTH_LONG).show()
        }

        return view
    }
}