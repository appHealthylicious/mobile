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
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipesItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecommendationItem
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class MenuFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var progressBar: View
    private var listItem1: MutableList<RecommendationItem?> = mutableListOf()
    private var listItem2: MutableList<RecommendationItem?> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var listMenuAdapter: ListMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRecommendationsByRate()
        viewModel.fetchRecipesRecommendation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        var combinedList: MutableList<RecommendationItem?> = mutableListOf()
        recyclerView = view.findViewById(R.id.menu_recycler_view1)
        progressBar = view.findViewById(R.id.progressBar)

        recyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.getRecipeRecommendationByRate().observe(viewLifecycleOwner) { response ->

            println("Recipes recommendation by rate")
            var recommendationItem = RecommendationItem(mutableListOf(), "Recommended for you")
            response?.let { items ->
                for (data in items) {
                    val recipesItem = RecipesItem(data.image, data.title)
                    recommendationItem.recipes?.add(recipesItem)
                }
            }
            listItem1.clear()
            listItem1.add(recommendationItem)
            viewModel.getRecipesRecommendation().observe(viewLifecycleOwner) { response ->
                println("Normal recipes recommendations")
                response?.recommendation?.let { recommendationList ->
                    listItem2.clear()
                    listItem2.addAll(recommendationList)
                }
            }

            if(listItem1.isNotEmpty() && listItem2.isNotEmpty()){
                combinedList.clear()
                combinedList = (listItem1+listItem2).toMutableList()

                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                listMenuAdapter = ListMenuAdapter(combinedList) { recipeTitle ->
                    navigateToRecipeDetails(recipeTitle)
                }
                recyclerView.adapter = listMenuAdapter

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
