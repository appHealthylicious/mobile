package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.IngredientGroupsItem
import com.google.android.material.textview.MaterialTextView

class RecipeDetailsIngredientsAdapter(
    private val item: List<IngredientGroupsItem?>?
) : RecyclerView.Adapter<RecipeDetailsIngredientsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: MaterialTextView = itemView.findViewById(R.id.detail_purpose)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.detail_recyclerview2)

        fun bind(
            dataItem: IngredientGroupsItem?
        ) {
            title.text = dataItem?.purpose
            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
            recyclerView.adapter = dataItem?.ingredients?.let { ItemRecipeDetailsIngredientsAdapter(it) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = item?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = item!![position]
        holder.bind(dataItem)
    }

}