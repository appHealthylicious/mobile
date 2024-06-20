package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.ItemRecipeDetailsInstructionsAdapter
import com.dicoding.picodiploma.loginwithanimation.data.adapter.RecipeDetailsIngredientsAdapter
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeDetailsResponseItem
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class RecipeDetailFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel>{
        MainModelFactory.getInstance(requireContext())
    }
    //private lateinit's
    private lateinit var recipeTitle: String
    private lateinit var recipeImage: ImageView
    private lateinit var recipeTitleDisplay: TextView
    private lateinit var recipeDescription: TextView
    private lateinit var ingredientsRecyclerView: RecyclerView
    private lateinit var instructionsRecyclerView: RecyclerView
    private lateinit var progressBar: View
    private lateinit var scrollView: ScrollView
    private lateinit var textCalorie: TextView
    private lateinit var textDuration: TextView
    private lateinit var btnToggle: ImageButton
    private lateinit var saveRecipeText: String
    private lateinit var textRecipeID: TextView
    private lateinit var rateButton: Button
    private lateinit var recipeId: String
    private var recipeName: String = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        recipeTitle = arguments?.getString("recipe_title") ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_detail, container, false)
        recipeImage = view.findViewById(R.id.recipe_img)
        recipeTitleDisplay = view.findViewById(R.id.recipe_title)
        recipeDescription = view.findViewById(R.id.recipe_description)
        ingredientsRecyclerView = view.findViewById(R.id.detail_ingredients_recyclerview)
        instructionsRecyclerView = view.findViewById(R.id.details_directions_recyclerview)
        progressBar = view.findViewById(R.id.progressBar)
        scrollView = view.findViewById(R.id.scrollview)
        textCalorie = view.findViewById(R.id.text_calorie)
        textDuration = view.findViewById(R.id.text_duration)
        btnToggle = view.findViewById(R.id.toggleButton)
        textRecipeID = view.findViewById(R.id.text_recipe_id)
        rateButton = view.findViewById(R.id.button_rate)

        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.GONE

        var imageUrl: String = ""

        ingredientsRecyclerView.layoutManager = LinearLayoutManager(context)
        instructionsRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.recipeDetails().observe(viewLifecycleOwner) { details ->
            details?.let {
                bindDetails(it[0])
                imageUrl = it[0].image!!
                saveRecipeText = "$recipeTitle|${imageUrl}"
                ingredientsRecyclerView.adapter = RecipeDetailsIngredientsAdapter(it[0].ingredientGroups)
                instructionsRecyclerView.adapter = ItemRecipeDetailsInstructionsAdapter(it[0].instructionsList)
                viewModel.getFavouriteRecipe().observe(viewLifecycleOwner) { favoriteRecipes ->
                    if (saveRecipeText in favoriteRecipes) {
                        btnToggle.isSelected = true
                    } else {
                        btnToggle.isSelected = false
                    }
                }
                progressBar.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
            }
        }

        viewModel.getRecipeDetails(recipeTitle)

        saveRecipeText = "$recipeTitle|${imageUrl}"

        progressBar.visibility = View.VISIBLE
        scrollView.visibility = View.GONE

        btnToggle.setOnClickListener {
            val isSelected = btnToggle.isSelected
            if (isSelected) {
                viewModel.deleteFavouriteRecipe(saveRecipeText)
            } else {
                viewModel.addFavouriteRecipe(saveRecipeText)
                println("Saved as $saveRecipeText")
            }
            btnToggle.isSelected = !isSelected
        }
        rateButton.setOnClickListener {
            showRatingDialog()
        }
        return view
    }
    private fun bindDetails(details: RecipeDetailsResponseItem){
        Glide.with(this).load(details.image).into(recipeImage)
        recipeTitleDisplay.text = details.title
        recipeDescription.text = details.description
        textCalorie.text = details.nutrients?.calories
        textDuration.text = "${details.totalTime} mins"
        textRecipeID.text = "RecipeID : ${details.recipeId}"
        recipeId = details.recipeId ?: ""
        recipeName = "Rate recipe: ${details.title}" ?: ""

    }

    private fun showRatingDialog(){
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.layout_rate_recipe, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        val stars = listOf(
            dialogView.findViewById<ImageView>(R.id.star1),
            dialogView.findViewById<ImageView>(R.id.star2),
            dialogView.findViewById<ImageView>(R.id.star3),
            dialogView.findViewById<ImageView>(R.id.star4),
            dialogView.findViewById<ImageView>(R.id.star5)
        )

        var selectedRating = 0

        stars.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                selectedRating = index + 1
                stars.forEachIndexed { starIndex, starImageView ->
                    starImageView.setImageResource(if (starIndex < selectedRating) R.drawable.star_yellow else R.drawable.star_grey)
                }
            }
        }

        dialogView.findViewById<Button>(R.id.button_submit).setOnClickListener {
            viewModel.postRating(recipeId, selectedRating.toString())
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    companion object {
        fun newInstance(recipeTitle: String) = RecipeDetailFragment().apply {
            arguments = Bundle().apply {
                putString("recipe_title", recipeTitle)
            }
        }
    }

}
