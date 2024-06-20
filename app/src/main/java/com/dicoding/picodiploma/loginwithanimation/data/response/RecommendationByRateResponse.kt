package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationByRateResponse(

	@field:SerializedName("RecommendationByRateResponse")
	val recommendationByRateResponse: List<RecommendationByRateResponseItem?>? = null
)

data class RecommendationByRateResponseItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("mean_rating")
	val meanRating: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("recipeId")
	val recipeId: String? = null,

	@field:SerializedName("rating_count")
	val ratingCount: Int? = null
)
