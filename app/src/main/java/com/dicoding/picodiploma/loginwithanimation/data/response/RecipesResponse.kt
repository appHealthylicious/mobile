package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class RecipesResponse(

	@field:SerializedName("RecipesResponse")
	val recipesResponse: List<RecipesResponseItem?>? = null
)

data class RecipesResponseItem(

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("Image")
	val image: String? = null
)
