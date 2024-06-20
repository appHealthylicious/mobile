package com.dicoding.picodiploma.loginwithanimation.view.main.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
    private lateinit var searchEditText: EditText
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
        val view = inflater.inflate(R.layout.fragment_recipe_search, container, false)
        recyclerView = view.findViewById(R.id.recipe_search_recycler_view)
        progressBar = view.findViewById(R.id.progressBar)
        emptyMessage = view.findViewById(R.id.emptyMessage)
        searchEditText = view.findViewById(R.id.search_edit_text)
        searchCount = view.findViewById(R.id.search_count)

        val gridLayoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = gridLayoutManager

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    s?.let {
                        performSearch(it.toString())
                    }
                }
                handler.postDelayed(searchRunnable!!, 300)
            }

            override fun afterTextChanged(s: Editable?) {}
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

        view.findViewById<ViewGroup>(R.id.parent_layout).setOnTouchListener { _, _ ->
            searchEditText.clearFocus()
            false
        }

        searchEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                v.hideKeyboard()
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
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
