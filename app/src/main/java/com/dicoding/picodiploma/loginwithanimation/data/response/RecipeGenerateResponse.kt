package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class RecipeGenerateResponse(

	@field:SerializedName("RecipeGenerateResponse")
	val recipeGenerateResponse: List<RecipeGenerateResponseItem?>? = null
)

data class RecipeGenerateResponseItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
