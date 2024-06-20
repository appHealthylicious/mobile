package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.MainRepository
import com.dicoding.picodiploma.loginwithanimation.data.dataclass.ChipItem
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _chipItems = MutableLiveData<List<ChipItem>>()
    val chipItems: LiveData<List<ChipItem>> get() = _chipItems

    fun ingredientsSearchResults() = repository.ingredientSearchResults.asLiveData()
    fun recipeSearchResults() = repository.recipeSearchResults.asLiveData()
    fun recipeDetails() = repository.recipeDetails.asLiveData()

    fun getIngredients() = repository.ingredients.asLiveData()
    fun getRecipesRecommendation() = repository.recipes.asLiveData()
    fun getGeneratedRecipes() = repository.recipeGenerateResults.asLiveData()
    fun getRecipeRecommendationByRate() = repository.recipeRecommendations.asLiveData()
    fun getDislikesData() = repository.getDislikesData.asLiveData()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    //TODO RESET
    fun getReset(){
        viewModelScope.launch {
            repository.getReset()
        }
    }

    //TODO Favourite Recipe
    fun getFavouriteRecipe(): LiveData<List<String>> {
        return repository.getFavouriteRecipe().asLiveData()
    }
    fun addFavouriteRecipe(recipeId: String) {
        viewModelScope.launch {
            repository.addFavouriteRecipe(recipeId)
        }

    }
    fun deleteFavouriteRecipe(recipeId: String) {
        viewModelScope.launch {
            repository.deleteFavouriteRecipe(recipeId)
        }
    }
    fun fetchRecipesRecommendation(){
        repository.fetchRecipesRecommendation()
    }
    fun searchRecipe(query: String){
        viewModelScope.launch {
            repository.searchRecipes(query)
        }
    }
    fun getRecipeDetails(title: String){
        viewModelScope.launch {
            repository.getRecipeDetails(title)
        }
    }
    fun generateRecipe(ingredients: String){
        viewModelScope.launch {
            repository.generateRecipe(ingredients)
        }
    }
    fun getRecommendationsByRate(){
        viewModelScope.launch {
            repository.getRecommendationbyRate()
        }
    }


    //TODO Ingredients
    fun getIngredientsLocal(): LiveData<List<String>> {
        return repository.getIngredientsLocal().asLiveData()
    }
    fun addIngredient(ingredientId: String) {
        viewModelScope.launch {
            repository.addIngredient(ingredientId)
            println("Added ingredient: $ingredientId")
        }
    }
    fun deleteIngredient(ingredientId: String) {
        viewModelScope.launch {
            repository.deleteIngredient(ingredientId)
            println("Removed ingredient: $ingredientId")
        }
    }
    fun fetchIngredients(){
        viewModelScope.launch {
            repository.fetchIngredientsFromApi()
        }
    }
    fun searchIngredients(query: String){
        viewModelScope.launch {
            repository.searchIngredients(query)
        }
    }
    fun getDislikeIngredients(): LiveData<List<String>> {
        return repository.getDislikeIngredients().asLiveData()
    }
    fun addDislikeIngredient(ingredientId: String){
        viewModelScope.launch {
            repository.addDislikeIngredient(ingredientId)
            println("Added dislike ingredient: $ingredientId")
        }
    }
    fun deleteDislikeIngredient(ingredientId: String){
        viewModelScope.launch {
            repository.deleteDislikeIngredient(ingredientId)
            println("Removed dislike ingredient: $ingredientId")
        }
    }

    fun postDislikes(ingredients: List<String>){
        viewModelScope.launch {
            repository.postDislikes(ingredients)
        }
    }

    fun postRating(recipeId: String, rating: String){
        viewModelScope.launch {
            repository.postRating(recipeId, rating)
        }
    }

    fun getDislikes(){
        viewModelScope.launch {
            repository.getDislikes()
        }
    }



}