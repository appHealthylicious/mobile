package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class RecipeSearchResponseItem(

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("Image")
	val image: String? = null
)


