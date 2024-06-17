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
import com.dicoding.picodiploma.loginwithanimation.data.adapter.ListPantryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.MainModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel


class PantryFragment : Fragment() {
    private val viewModel by viewModels<MainViewModel> {
        MainModelFactory.getInstance(requireContext())
    }
    private lateinit var listPantryAdapter: ListPantryAdapter
    private lateinit var progressBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchIngredients()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pantry, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.pantry_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        viewModel.getIngredients().observe(viewLifecycleOwner) { response ->
            response?.let {
                viewModel.getIngredientsLocal().observe(viewLifecycleOwner) { favoriteIngredients ->
                    if (!::listPantryAdapter.isInitialized) {
                        listPantryAdapter = ListPantryAdapter(
                            it,
                            favoriteIngredients
                        ) { ingredient, isChecked ->
                            if (isChecked) {
                                viewModel.addIngredient(ingredient)
                            } else {
                                viewModel.deleteIngredient(ingredient)
                            }
                        }
                        recyclerView.adapter = listPantryAdapter
                        progressBar.visibility = View.GONE
                    } else {
                        listPantryAdapter.updateIngredients(favoriteIngredients)
                    }
                }
            }
        }

        return view
    }


}