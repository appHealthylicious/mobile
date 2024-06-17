package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeSearchResponseItem

class RecipeSearchAdapter(
    private var results: List<RecipeSearchResponseItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecipeSearchAdapter.ViewHolder>() {
    class ViewHolder(
        itemView: View,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.menu_title)
        val image: ImageView = itemView.findViewById(R.id.menu_image_view)

        fun bind(menuItem: RecipeSearchResponseItem){
            title.text = menuItem.title
            Glide.with(itemView.context)
                .load(menuItem.image)
                .into(image)

            itemView.setOnClickListener {
                menuItem.title?.let { onItemClick(it) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recipe_search, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        results.get(position)?.let {
            holder.bind(it)
        }
    }
    fun updateData(newResults: List<RecipeSearchResponseItem>?) {
        if(newResults != null){
            results = newResults
        }
        notifyDataSetChanged()
    }
}