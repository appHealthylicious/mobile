package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.RecommendationItem

class ListMenuAdapter(
    private val menuLists: List<RecommendationItem?>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ListMenuAdapter.ListViewHolder>() {
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.menu_title)
        val subtitle: TextView = itemView.findViewById(R.id.menu_subtitle)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.menu_recycler_view2)
        fun bind(
            menuItem: RecommendationItem,
            onItemClick: (String) -> Unit
        ) {
            val menuList = menuItem.recipes
            title.text = menuItem.category
            subtitle.text = "${menuList?.size} menu available"
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            recyclerView.layoutManager = staggeredGridLayoutManager
            recyclerView.adapter = ItemMenuAdapter(menuList, onItemClick)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = menuLists.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val menuItem = menuLists[position]
        if (menuItem != null) {
            holder.bind(menuItem, onItemClick)
        }
    }
}