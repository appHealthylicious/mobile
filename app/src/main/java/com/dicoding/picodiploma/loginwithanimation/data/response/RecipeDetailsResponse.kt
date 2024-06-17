package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName



data class IngredientGroupsItem(

	@field:SerializedName("purpose")
	val purpose: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String?>? = null
)

data class Nutrients(

	@field:SerializedName("sugarContent")
	val sugarContent: String? = null,

	@field:SerializedName("proteinContent")
	val proteinContent: String? = null,

	@field:SerializedName("fiberContent")
	val fiberContent: String? = null,

	@field:SerializedName("unsaturatedFatContent")
	val unsaturatedFatContent: String? = null,

	@field:SerializedName("fatContent")
	val fatContent: String? = null,

	@field:SerializedName("transFatContent")
	val transFatContent: String? = null,

	@field:SerializedName("cholesterolContent")
	val cholesterolContent: String? = null,

	@field:SerializedName("calories")
	val calories: String? = null,

	@field:SerializedName("carbohydrateContent")
	val carbohydrateContent: String? = null,

	@field:SerializedName("servingSize")
	val servingSize: String? = null,

	@field:SerializedName("saturatedFatContent")
	val saturatedFatContent: String? = null,

	@field:SerializedName("sodiumContent")
	val sodiumContent: String? = null
)

data class RecipeDetailsResponseItem(

	@field:SerializedName("recipeId")
	val recipeId : String? = null,

	@field:SerializedName("instructions")
	val instructions: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("yields")
	val yields: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("total_time")
	val totalTime: Int? = null,

	@field:SerializedName("ingredient_groups")
	val ingredientGroups: List<IngredientGroupsItem?>? = null,

	@field:SerializedName("instructions_list")
	val instructionsList: List<String?>? = null,

	@field:SerializedName("nutrients")
	val nutrients: Nutrients? = null
)
