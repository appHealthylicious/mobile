package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R

class FavouriteRecipeAdapter(
    private var results: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<FavouriteRecipeAdapter.ViewHolder>() {
    class ViewHolder(
        itemView: View,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.menu_title)
        val image: ImageView = itemView.findViewById(R.id.menu_image_view)

        fun bind(menuItem: String){
            val parts = menuItem.split("|")
            title.text = parts[0]
            Glide.with(itemView.context)
                .load(parts[1])
                .into(image)

            itemView.setOnClickListener {
                menuItem.let { onItemClick(it) }
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
    fun updateData(newResults: List<String>?) {
        if(newResults != null){
            results = newResults
        }
        notifyDataSetChanged()
    }
    fun splitString(input: String): Pair<String, String> {
        val parts = input.split("|")
        return Pair(parts[0], parts[1])
    }
}