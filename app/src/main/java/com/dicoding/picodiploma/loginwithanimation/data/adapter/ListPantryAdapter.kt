package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.IngredientResponseItem
import com.google.android.material.textview.MaterialTextView

class ListPantryAdapter(
    private val ingredientResponse: List<IngredientResponseItem>,
    private var listedIngredients: List<String>,
    private val onChipClick: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ListPantryAdapter.ListViewHolder>() {

    private lateinit var itemPantryAdapter: ItemPantryAdapter

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: MaterialTextView = itemView.findViewById(R.id.title_recycler_view)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.item_recycler_view)
        val subtitle: TextView = itemView.findViewById(R.id.recipe_description)

        fun bind(
            ingredientItem: IngredientResponseItem,
            listSize: Int,
            listedIngredients: List<String>,
            onChipClick: (String, Boolean) -> Unit
        ) {
            title.text = ingredientItem.category
            subtitle.text = "$listSize ingredients available"

            println(subtitle.text)

            val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
            recyclerView.layoutManager = staggeredGridLayoutManager

            val itemPantryAdapter = ItemPantryAdapter(ingredientItem.items ?: emptyList(), listedIngredients, onChipClick)
            recyclerView.adapter = itemPantryAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pantry, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = ingredientResponse.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val ingredientItem = ingredientResponse[position]
        val count = ingredientResponse[position].items?.size ?: 0
        holder.bind(ingredientItem, count, listedIngredients, onChipClick)
    }

    fun updateIngredients(newIngredients: List<String>) {
        listedIngredients = newIngredients
        notifyItemRangeChanged(0, itemCount, newIngredients)
    }
}
