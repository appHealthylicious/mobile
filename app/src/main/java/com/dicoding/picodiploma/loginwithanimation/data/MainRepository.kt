package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.api.Api
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.IngredientResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeDetailsResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeGenerateResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeSearchResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipesRecommendationResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository private constructor (
    val apiService: Api,
    private val userPreference: UserPreference
    ){
    private val _ingredients = MutableStateFlow<List<IngredientResponseItem>?>(null)
    val ingredients: Flow<List<IngredientResponseItem>?> get() = _ingredients

    private val _recipes = MutableStateFlow<RecipesRecommendationResponse?>(null)
    val recipes: Flow<RecipesRecommendationResponse?> get() = _recipes

    private val _ingredientSearchResults = MutableStateFlow<List<String>>(emptyList())
    val ingredientSearchResults: Flow<List<String>> get() = _ingredientSearchResults

    private val _recipeSearchResults = MutableStateFlow<List<RecipeSearchResponseItem>?>(null)
    val recipeSearchResults: Flow<List<RecipeSearchResponseItem>?> get() = _recipeSearchResults

    private val _recipeDetails = MutableStateFlow<List<RecipeDetailsResponseItem>?>(null)
    val recipeDetails: Flow<List<RecipeDetailsResponseItem>?> get() = _recipeDetails

    private val _recipeGenerateResults = MutableStateFlow<List<RecipeGenerateResponseItem>?>(null)
    val recipeGenerateResults: Flow<List<RecipeGenerateResponseItem>?> get() = _recipeGenerateResults

    suspend fun logout() {
        userPreference.logout()
    }
    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    //TODO RESET
    suspend fun getReset(){
        userPreference.resetFavouriteRecipes()
        userPreference.resetBasketIngredients()
    }

    //TODO Favourite Recipe
    fun getFavouriteRecipe(): Flow<List<String>> {
        return userPreference.getFavouriteRecipes()
    }
    suspend fun addFavouriteRecipe(recipeId: String) {
        userPreference.addFavouriteRecipe(recipeId)
    }
    suspend fun deleteFavouriteRecipe(recipeId: String) {
        userPreference.deleteFavouriteRecipe(recipeId)
    }
    fun fetchRecipesRecommendation(){
        apiService.getRecommendationRecipe().enqueue(object : Callback<RecipesRecommendationResponse>{
            override fun onResponse(
                call: Call<RecipesRecommendationResponse>,
                response: Response<RecipesRecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    _recipes.value = response.body()
                    println("Saved ${response.body()}")
                } else {
                    _recipes.value = null
                    println("Response isn't successful")
                }
            }

            override fun onFailure(call: Call<RecipesRecommendationResponse>, t: Throwable) {
                _recipes.value = null
                println("Failed to response")
            }

        })
    }
    fun searchRecipes(query: String){
        apiService.searchRecipes(query).enqueue(object: Callback<List<RecipeSearchResponseItem>>{
            override fun onResponse(
                call: Call<List<RecipeSearchResponseItem>>,
                response: Response<List<RecipeSearchResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _recipeSearchResults.value = response.body() ?: emptyList()
                    println("SUCCESSFUL RESPONSE")
                } else {
                    _recipeSearchResults.value = emptyList()
                    println("fail response body")
                }
            }

            override fun onFailure(call: Call<List<RecipeSearchResponseItem>>, t: Throwable) {
                _recipeSearchResults.value = emptyList()
                println("Failed to respond")
            }

        })
    }
    fun getRecipeDetails(title:String){
        apiService.getRecipeDetails(title).enqueue(object: Callback<List<RecipeDetailsResponseItem>>{
            override fun onResponse(
                call: Call<List<RecipeDetailsResponseItem>>,
                response: Response<List<RecipeDetailsResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _recipeDetails.value = response.body()
                    println("Saved ${response.body()}")
                } else {
                    _recipeDetails.value = null
                    println("Response isn't successful")
                }
            }

            override fun onFailure(call: Call<List<RecipeDetailsResponseItem>>, t: Throwable) {
                _recipeDetails.value = null
                println("Response isn't successful")
            }

        })
    }
    fun generateRecipe(ingredients: String){
        apiService.generateRecipe(ingredients).enqueue(object: Callback<List<RecipeGenerateResponseItem>>{
            override fun onResponse(
                call: Call<List<RecipeGenerateResponseItem>>,
                response: Response<List<RecipeGenerateResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _recipeGenerateResults.value = response.body()
                    println("Saved ${response.body()}")
                } else {
                    _recipeGenerateResults.value = null
                    println("Response isn't successful 2")
                }
            }

            override fun onFailure(call: Call<List<RecipeGenerateResponseItem>>, t: Throwable) {
                _recipeGenerateResults.value = null
                println("Response isn't successful 1")
            }

        })
    }

    //TODO Ingredients

    fun getIngredientsLocal(): Flow<List<String>> {
        return userPreference.getIngredientsLocal()
    }
    suspend fun addIngredient(ingredientId: String) {
        userPreference.addIngredient(ingredientId)
    }
    suspend fun deleteIngredient(ingredientId: String) {
        userPreference.deleteIngredient(ingredientId)
    }
    fun fetchIngredientsFromApi() {
        apiService.getIngredientsApi().enqueue(object : Callback<List<IngredientResponseItem>>{
            override fun onResponse(
                call: Call<List<IngredientResponseItem>>,
                response: Response<List<IngredientResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _ingredients.value = response.body()
                    println("Saved ${response.body()}")
                } else {
                    _ingredients.value = null
                    println("Response isn't successful")
                }
            }

            override fun onFailure(call: Call<List<IngredientResponseItem>>, t: Throwable) {
                _ingredients.value = null
                println("Failed to response 123")
            }

        })
    }
    fun searchIngredients(query: String){
        apiService.searchIngredients(query).enqueue(object: Callback<List<String>>{
            override fun onResponse(
                call: Call<List<String>>, response: Response<List<String>>
            ) {
                if (response.isSuccessful) {
                    _ingredientSearchResults.value = response.body() ?: emptyList()
                } else {
                    _ingredientSearchResults.value = emptyList()
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                _ingredientSearchResults.value = emptyList()
            }

        })

    }

    companion object {
        fun getInstance(
            apiService: Api,
            userPref: UserPreference
        ) = MainRepository(apiService, userPref)
    }
}