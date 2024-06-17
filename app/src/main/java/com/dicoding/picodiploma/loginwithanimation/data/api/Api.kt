package com.dicoding.picodiploma.loginwithanimation.data.api

import com.dicoding.picodiploma.loginwithanimation.data.response.IngredientResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ProfileResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeDetailsResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeGenerateResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipeSearchResponseItem
import com.dicoding.picodiploma.loginwithanimation.data.response.RecipesRecommendationResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.UpdateResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("user/profile/{uid}")
    fun getUserProfile(
        @Path("uid") uid: String
    ): Call<ProfileResponse>

    @FormUrlEncoded
    @PUT("user/profile")
    fun updateUserProfile(
        @Field("username") username: String,
        @Field("age") age: String,
        @Field("weight") weight: String,
        @Field("height") height: String
    ): Call<UpdateResponse>

    @GET("ingredients")
    fun getIngredientsApi(): Call<List<IngredientResponseItem>>

    @GET("recommendations")
    fun getRecommendationRecipe(): Call<RecipesRecommendationResponse>

    @GET("ingredients/search")
    fun searchIngredients(
        @Query("query") query: String
    ): Call<List<String>>

    @GET("recipes/search")
    fun searchRecipes(
        @Query("query") query: String
    ): Call<List<RecipeSearchResponseItem>>

    @GET("recipes/{title}")
    fun getRecipeDetails(
        @Path("title") title: String
    ): Call<List<RecipeDetailsResponseItem>>

    @FormUrlEncoded
    @POST("generate")
    fun generateRecipe(
        @Field("ingredients") ingredients: String
    ): Call<List<RecipeGenerateResponseItem>>
}