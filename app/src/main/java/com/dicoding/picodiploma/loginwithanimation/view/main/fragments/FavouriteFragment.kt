package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.FavouriteRecipeAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class FavouriteFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var favouriteAdapter: FavouriteRecipeAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        recyclerView = view.findViewById(R.id.favourite_recycler_view)
        emptyMessage = view.findViewById(R.id.title_favourite_empty)

        recyclerView.layoutManager = GridLayoutManager(context, 2)
        favouriteAdapter = FavouriteRecipeAdapter(emptyList()) { recipeTitle ->
            val parts = recipeTitle.split("|")
            navigateToRecipeDetails(parts[0])
        }
        recyclerView.adapter = favouriteAdapter

        viewModel.getFavouriteRecipe().observe(viewLifecycleOwner) { favouriteRecipes ->
            if (favouriteRecipes.isNotEmpty()) {
                emptyMessage.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                favouriteAdapter.updateData(favouriteRecipes)
            } else {
                emptyMessage.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }

        return view
    }

    private fun navigateToRecipeDetails(recipeTitle: String) {
        val fragment = RecipeDetailFragment.newInstance(recipeTitle)
        (activity as? MainActivity)?.hideProfile()
        (activity as? MainActivity)?.hideSearch()
        (activity as? MainActivity)?.hideBack()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
