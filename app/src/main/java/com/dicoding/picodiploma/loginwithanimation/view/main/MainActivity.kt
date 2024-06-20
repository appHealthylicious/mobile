package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.FavouriteFragment
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.IngredientSearchFragment
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.MenuFragment
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.PantryFragment
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.ProfileFragment
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.RecipeSearchFragment
import com.dicoding.picodiploma.loginwithanimation.view.main.fragments.ShopFragment
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private var lastFragment: Fragment? = MenuFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLoggedIn) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupFragment()
        setupView()
        setupAction()

        binding.bottomNavigationView.selectedItemId = R.id.home
    }

    private fun setupFragment(){
        replaceFragment(MenuFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    replaceFragment(MenuFragment())
                    lastFragment = MenuFragment()
                    showSearch()
                    hideBack()
                    showProfile()
                    true
                }
                R.id.pantri -> {
                    replaceFragment(PantryFragment())
                    lastFragment = PantryFragment()
                    showSearch()
                    hideBack()
                    showProfile()
                    true
                }
                R.id.favorit -> {
                    replaceFragment(FavouriteFragment())
                    lastFragment = FavouriteFragment()
                    hideSearch()
                    hideBack()
                    showProfile()
                    true
                }
                R.id.shop -> {
                    replaceFragment(ShopFragment())
                    lastFragment = ShopFragment()
                    hideSearch()
                    hideBack()
                    showProfile()
                    true
                }
                else -> false
            }
        }
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
        binding.profileImageView.setOnClickListener {
            replaceFragment(ProfileFragment())
            hideSearch()
            hideProfile()
            showBack()
        }
        binding.backButtonView.setOnClickListener {
            replaceFragment(lastFragment!!)
            binding.profileImageView.visibility = View.VISIBLE
            binding.backButtonView.visibility = View.GONE
            when(lastFragment) {
                is PantryFragment -> showSearch()
                is MenuFragment -> showSearch()
            }
        }
        binding.searchButton.setOnClickListener {
            when (lastFragment) {
                is PantryFragment -> replaceFragment(IngredientSearchFragment())
                is MenuFragment -> replaceFragment(RecipeSearchFragment())
            }
            hideSearch()
            hideProfile()
            showBack()
        }
    }

    fun showSearch(){
        binding.searchButton.visibility = View.VISIBLE
    }
    fun hideSearch(){
        binding.searchButton.visibility = View.GONE
    }
    fun hideProfile(){
        binding.profileImageView.visibility = View.GONE
    }
    fun showProfile(){
        binding.profileImageView.visibility = View.VISIBLE
    }
    fun showBack(){
        binding.backButtonView.visibility = View.VISIBLE
    }
    fun hideBack(){
        binding.backButtonView.visibility = View.GONE
    }
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

}