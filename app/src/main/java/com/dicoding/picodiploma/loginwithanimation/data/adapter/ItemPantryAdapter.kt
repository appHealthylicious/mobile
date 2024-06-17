package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.material.chip.Chip

class ItemPantryAdapter(
    private var pantryItems: List<String?>,
    private var listedIngredients: List<String>,
    private val onChipClick: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ItemPantryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chip: Chip = itemView.findViewById(R.id.chip)

        fun bind(text: String, isListed: Boolean, onChipClick: (String, Boolean) -> Unit) {
            chip.text = text
            chip.isChecked = isListed
            chip.setOnCheckedChangeListener { _, isChecked ->
                onChipClick(text, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = pantryItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = pantryItems[position]
        val isListed = listedIngredients.contains(ingredient)
        if (ingredient != null) {
            holder.bind(ingredient, isListed, onChipClick)
        }
    }

    fun updateData(newPantryItems: List<String?>) {
        pantryItems = newPantryItems
        notifyDataSetChanged()
    }
}
