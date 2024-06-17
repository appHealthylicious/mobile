package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.material.textview.MaterialTextView

class ItemRecipeDetailsIngredientsAdapter(
    private val item: List<String?>?
) : RecyclerView.Adapter<ItemRecipeDetailsIngredientsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: MaterialTextView = itemView.findViewById(R.id.list_item)

        fun bind(
            dataItem: String?
        ) {
            title.text = dataItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recipe_details, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = item?.size!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = item!![position]
        println("DATA ITEM : $dataItem")
        holder.bind(dataItem)
    }

}