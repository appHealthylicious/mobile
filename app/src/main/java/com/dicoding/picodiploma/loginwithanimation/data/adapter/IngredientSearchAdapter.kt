package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.material.chip.Chip

class IngredientSearchAdapter(
    private var results: List<String>,
    private var listedIngredients: List<String>,
    private val onChipClick: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<IngredientSearchAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chip: Chip = itemView.findViewById(R.id.chip)

        fun bind(chipText: String, isListed: Boolean, onChipClick: (String, Boolean) -> Unit){
            chip.text = chipText
            chip.setOnCheckedChangeListener(null) // Remove previous listener
            chip.isChecked = isListed
            chip.setOnCheckedChangeListener { _, isChecked ->
                onChipClick(chipText, isChecked)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_ingredient, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = results.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient = results[position]
        val isListed = listedIngredients.contains(ingredient)
        holder.bind(ingredient, isListed, onChipClick)
    }
    fun updateIngredients(newResults: List<String>, newIngredients: List<String>) {
        listedIngredients = newIngredients
        results = newResults
        notifyDataSetChanged()
    }
}