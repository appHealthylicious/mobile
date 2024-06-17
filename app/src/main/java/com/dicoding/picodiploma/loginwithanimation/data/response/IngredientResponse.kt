package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class IngredientResponse(

	@field:SerializedName("IngredientResponse")
	val ingredientResponse: List<IngredientResponseItem?>? = null
)

data class IngredientResponseItem(

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("items")
	val items: List<String?>? = null
)
