package com.dicoding.picodiploma.loginwithanimation.view.main.fragments.dislike

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.adapter.IngredientSearchAdapter
import com.dicoding.picodiploma.loginwithanimation.data.adapter.ItemDislikeAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class DislikeFragment: Fragment() {
    private val viewModel by viewModels<MainViewModel>{
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var ingredientSearchAdapter: IngredientSearchAdapter
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var localRecyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var adapter2: ItemDislikeAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchEmpty: View
    private lateinit var searchEmpty2: View
    private lateinit var searchCount: TextView
    private lateinit var saveButton: Button

    private var ingredientsList: List<String> = listOf()
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        val view = inflater.inflate(R.layout.fragment_dislike, container, false)
        searchRecyclerView = view.findViewById(R.id.dislike_search_recycler_view)
        localRecyclerView = view.findViewById(R.id.dislike_local_recycler_view)
        searchEditText = view.findViewById(R.id.search_edit_text)
        progressBar = view.findViewById(R.id.progressBar)
        searchEmpty = view.findViewById(R.id.title_search_dislike_empty)
        searchEmpty2 = view.findViewById(R.id.dislikes_local_empty)
        searchCount = view.findViewById(R.id.search_count_text)
        saveButton = view.findViewById(R.id.button_save)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
        searchRecyclerView.layoutManager = staggeredGridLayoutManager

        val staggeredGridLayoutManager2 = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
        localRecyclerView.layoutManager = staggeredGridLayoutManager2

        setupSearchEditText()

        viewModel.ingredientsSearchResults().observe(viewLifecycleOwner) { searchResults ->
            searchEmpty.visibility = View.GONE
            progressBar.visibility = View.GONE
            if (searchResults.isNotEmpty()) {
                searchCount.text = "Results: ${searchResults.size} ingredients available"
                searchRecyclerView.visibility = View.VISIBLE

                viewModel.getDislikeIngredients().observe(viewLifecycleOwner) { listIngredients ->
                    if(!::ingredientSearchAdapter.isInitialized){
                        ingredientSearchAdapter = IngredientSearchAdapter(searchResults, listIngredients){ ingredient, isChecked ->
                            if(isChecked){
                                viewModel.addDislikeIngredient(ingredient)
                                println("Added $ingredient")
                            } else {
                                viewModel.deleteDislikeIngredient(ingredient)
                                println("Removed $ingredient")
                            }
                        }
                        searchRecyclerView.adapter = ingredientSearchAdapter
                    } else {
                        ingredientSearchAdapter.updateIngredients(searchResults, listIngredients)
                    }
                }
            } else {

                searchCount.text = "Results: 0 ingredients available"
                progressBar.visibility = View.GONE
                searchEmpty.visibility = View.VISIBLE
                searchRecyclerView.visibility = View.GONE
            }
        }

        adapter2 = ItemDislikeAdapter(listOf()) { item ->
            viewModel.deleteDislikeIngredient(item)
        }
        localRecyclerView.adapter = adapter2

        viewModel.getDislikeIngredients().observe(viewLifecycleOwner) { ingredients ->
            if (ingredients.isEmpty()){
                searchEmpty2.visibility = View.VISIBLE
                localRecyclerView.visibility = View.GONE
            } else{
                searchEmpty2.visibility = View.GONE
                localRecyclerView.visibility = View.VISIBLE
                adapter2.updateItems(ingredients)
            }
        }

        saveButton.setOnClickListener {
            viewModel.getDislikeIngredients().observe(viewLifecycleOwner){ ingredients ->
                viewModel.postDislikes(ingredients)
                ingredientsList = ingredients
            }
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Yeah!")
                setMessage("Dislikes posted: ${ingredientsList.joinToString(", ")}.")
                setPositiveButton("Oke bro") { _, _ ->

                }
                create()
                show()
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
    private fun setupSearchEditText() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchRunnable?.let { handler.removeCallbacks(it) }
                searchRunnable = Runnable {
                    s?.let {
                        performSearch(it.toString())
                    }
                }
                handler.postDelayed(searchRunnable!!, 300) // 300ms debounce time
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
    private fun performSearch(query: String) {
        progressBar.visibility = View.VISIBLE
        searchEmpty.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        viewModel.searchIngredients(query)
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}