package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.IngredientSearchAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel


class IngredientSearchFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var ingredientSearchAdapter: IngredientSearchAdapter
    private lateinit var progressBar: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: View
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchCount: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredient_search, container, false)
        recyclerView = view.findViewById(R.id.pantry_earch_recycler_view)
        progressBar = view.findViewById(R.id.progressBar)
        emptyMessage = view.findViewById(R.id.emptyMessage)
        searchView = view.findViewById(R.id.searchView)
        searchCount = view.findViewById(R.id.search_count)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    newText?.let {
                        performSearch(it)
                    }
                }
                handler.postDelayed(searchRunnable!!, 300) // 300ms debounce time
                return true
            }
        })

        viewModel.ingredientsSearchResults().observe(viewLifecycleOwner) { searchResults ->
            emptyMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            if (searchResults.isNotEmpty()) {
                searchCount.text = "Results: ${searchResults.size} ingredients available"
                recyclerView.visibility = View.VISIBLE

                viewModel.getIngredientsLocal().observe(viewLifecycleOwner) { listIngredients ->
                    if(!::ingredientSearchAdapter.isInitialized){
                        ingredientSearchAdapter = IngredientSearchAdapter(searchResults, listIngredients){ ingredient, isChecked ->
                            if(isChecked){
                                viewModel.addIngredient(ingredient)
                                println("Added $ingredient")
                            } else {
                                viewModel.deleteIngredient(ingredient)
                                println("Removed $ingredient")
                            }
                        }
                        recyclerView.adapter = ingredientSearchAdapter
                    } else {
                        ingredientSearchAdapter.updateIngredients(searchResults, listIngredients)
                    }
                }
            } else {

                searchCount.text = "Results: 0 ingredients available"
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
        viewModel.searchIngredients(query)
    }

}
