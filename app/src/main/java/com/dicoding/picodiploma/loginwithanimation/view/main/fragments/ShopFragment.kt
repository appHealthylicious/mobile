package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.ItemShopAdapter
import com.dicoding.picodiploma.loginwithanimation.data.adapter.ShopResultsAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class ShopFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var basketMainLayout: View
    private lateinit var titleBasketEmpty: View
    private lateinit var generateRecipeButton: Button
    private lateinit var adapter: ItemShopAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var ingredientsList: String
    private var originalButtonColor: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)
        recyclerView = view.findViewById(R.id.basket_recycler_view)
        basketMainLayout = view.findViewById(R.id.basket_main_layout)
        titleBasketEmpty = view.findViewById(R.id.title_basket_empty)
        generateRecipeButton = view.findViewById(R.id.generate_recipe_button)
        resultsRecyclerView = view.findViewById(R.id.results_recyclerview)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        originalButtonColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        resultsRecyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = ItemShopAdapter(listOf()) { item ->
            viewModel.deleteIngredient(item)
        }
        recyclerView.adapter = adapter

        viewModel.getIngredientsLocal().observe(viewLifecycleOwner) { ingredients ->
            if (ingredients.isEmpty()) {
                hideUI()
            } else {
                showUI()
                adapter.updateItems(ingredients)
                ingredientsList = ingredients.joinToString(separator = ", ")
                println(ingredientsList)
            }
        }

        viewModel.getGeneratedRecipes().observe(viewLifecycleOwner){results ->
            progressBar.visibility = View.GONE
            resultsRecyclerView.visibility = View.VISIBLE
            resultsRecyclerView.adapter = ShopResultsAdapter(results) { recipeTitle ->
                navigateToRecipeDetails(recipeTitle)
            }

        }

        generateRecipeButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            resultsRecyclerView.visibility = View.GONE
            viewModel.generateRecipe(ingredientsList)
        }

        return view
    }

    fun showUI(){
        titleBasketEmpty.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        revertButtonColor()
    }
    fun hideUI(){
        titleBasketEmpty.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        changeButtonColorToGrey()
    }
    fun changeButtonColorToGrey() {
        generateRecipeButton.setBackgroundColor(Color.GRAY)
        generateRecipeButton.visibility = View.VISIBLE
        generateRecipeButton.isClickable = false
    }
    fun revertButtonColor() {
        generateRecipeButton.setBackgroundColor(originalButtonColor)
        generateRecipeButton.visibility = View.VISIBLE
        generateRecipeButton.isClickable = true
    }
    private fun navigateToRecipeDetails(recipeTitle: String) {
        val fragment = RecipeDetailFragment.newInstance(recipeTitle)
        (activity as? MainActivity)?.hideProfile()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}