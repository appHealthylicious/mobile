package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipesItem

class ItemMenuAdapter(
    private val menuItems: List<RecipesItem?>?,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ItemMenuAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.menu_title)
        val image: ImageView = itemView.findViewById(R.id.menu_image_view)

        fun bind(menuItem: RecipesItem,
                 onItemClick: (String) -> Unit){
            title.text = menuItem.title
            Glide.with(itemView.context)
                .load(menuItem.image)
                .into(image)

            itemView.setOnClickListener{
                menuItem.title?.let { onItemClick(it) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recipe, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = menuItems?.size ?: 0

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        menuItems?.get(position)?.let { holder.bind(it, onItemClick) }
    }
}