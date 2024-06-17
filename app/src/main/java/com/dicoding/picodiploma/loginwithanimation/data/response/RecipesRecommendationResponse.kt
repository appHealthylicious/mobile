package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class RecipesRecommendationResponse(

	@field:SerializedName("recommendation")
	val recommendation: List<RecommendationItem?>? = null
)

data class RecommendationItem(

	@field:SerializedName("recipes")
	val recipes: List<RecipesItem?>? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class RecipesItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)
