package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.RecipeSearchAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class RecipeSearchFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var recipeSearchAdapter: RecipeSearchAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_search, container, false)
        recyclerView = view.findViewById(R.id.recipe_search_recycler_view)
        progressBar = view.findViewById(R.id.progressBar)
        emptyMessage = view.findViewById(R.id.emptyMessage)
        searchView = view.findViewById(R.id.searchView)
        searchCount = view.findViewById(R.id.search_count)

        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(it)
                }
                return true
            }
        })

        viewModel.recipeSearchResults().observe(viewLifecycleOwner) {searchResults ->
            emptyMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            if (searchResults != null) {
                searchCount.text = "Results: ${searchResults.size} recipes available"
                recyclerView.visibility = View.VISIBLE
                recipeSearchAdapter = RecipeSearchAdapter(searchResults) { recipeTitle ->
                    navigateToRecipeDetails(recipeTitle)
                }
                recyclerView.adapter = recipeSearchAdapter
            } else {

                searchCount.text = "Results: 0 recipes available"
                progressBar.visibility = View.GONE
                emptyMessage.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }

        return view
    }

    private fun performSearch(query: String) {
        progressBar.visibility = View.VISIBLE
        emptyMessage.visibility = View.GONE
        recyclerView.visibility = View.GONE
        viewModel.searchRecipe(query)
    }
    private fun navigateToRecipeDetails(recipeTitle: String) {
        val fragment = RecipeDetailFragment.newInstance(recipeTitle)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
