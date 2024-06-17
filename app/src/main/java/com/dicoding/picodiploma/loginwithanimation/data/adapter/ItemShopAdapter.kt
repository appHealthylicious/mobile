package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R

class ItemShopAdapter(
    private var shopItems: List<String>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<ItemShopAdapter.listViewHolder>() {
    class listViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardText: TextView = itemView.findViewById(R.id.basket_card_text)
        val cardDelete: ImageView = itemView.findViewById(R.id.card_delete)


        fun bind(text: String, onDeleteClick: (String) -> Unit){
            cardText.text = text
            cardDelete.setOnClickListener {
                onDeleteClick(text)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredient_basket, parent, false)
        return listViewHolder(view)
    }


    override fun getItemCount(): Int = shopItems.size

    override fun onBindViewHolder(holder: listViewHolder, position: Int) {
        val item = shopItems[position]
        holder.bind(item, onDeleteClick)
    }
    fun updateItems(newItems: List<String>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = shopItems.size
            override fun getNewListSize(): Int = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return shopItems[oldItemPosition] == newItems[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return shopItems[oldItemPosition] == newItems[newItemPosition]
            }
        })
        shopItems = newItems
        diffResult.dispatchUpdatesTo(this)
    }
}