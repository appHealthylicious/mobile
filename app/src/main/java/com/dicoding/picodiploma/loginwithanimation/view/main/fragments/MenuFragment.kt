package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.ListMenuAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class MenuFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var progressBar: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchRecipesRecommendation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.menu_recycler_view1)
        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.getRecipesRecommendation().observe(viewLifecycleOwner) { response ->
            response?.recommendation?.let {recommendationList ->

                recyclerView.adapter = ListMenuAdapter(recommendationList) { recipeTitle ->
                    navigateToRecipeDetails(recipeTitle)
                }
                progressBar.visibility = View.GONE
            }
        }
        progressBar.visibility = View.VISIBLE

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