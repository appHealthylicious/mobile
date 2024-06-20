package com.dicoding.picodiploma.loginwithanimation.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[UID_KEY] = user.uid
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[LOGIN_KEY] = true
        }
    }

    suspend fun saveUserProfile(user: UserProfile) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = user.username
            preferences[AGE_KEY] = user.age
            preferences[WEIGHT_KEY] = user.weight
            preferences[HEIGHT_KEY] = user.height
        }
    }

    fun getUid(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[UID_KEY] ?: ""
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[UID_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[LOGIN_KEY] ?: false,
                UserProfile(
                    preferences[USERNAME_KEY] ?: "",
                    preferences[AGE_KEY] ?: "",
                    preferences[WEIGHT_KEY] ?: "",
                    preferences[HEIGHT_KEY] ?: ""
                )

            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // Favorite recipes functions
    fun getFavouriteRecipes(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[FAVORITE_RECIPES_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }
    }

    suspend fun addFavouriteRecipe(recipeId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_RECIPES_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentFavorites.add(recipeId)
            preferences[FAVORITE_RECIPES_KEY] = currentFavorites.joinToString(",")
        }
    }

    suspend fun deleteFavouriteRecipe(recipeId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_RECIPES_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentFavorites.remove(recipeId)
            preferences[FAVORITE_RECIPES_KEY] = currentFavorites.joinToString(",")
        }
    }

    // Favorite ingredients functions
    fun getIngredientsLocal(): Flow<List<String>> {
        return dataStore.data.map { preferences ->
            preferences[BASKET_INGREDIENTS_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }
    }

    suspend fun addIngredient(ingredientId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[BASKET_INGREDIENTS_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentFavorites.add(ingredientId)
            preferences[BASKET_INGREDIENTS_KEY] = currentFavorites.joinToString(",")
        }
    }

    suspend fun deleteIngredient(ingredientId: String) {
        dataStore.edit { preferences ->
            val currentFavorites = preferences[BASKET_INGREDIENTS_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentFavorites.remove(ingredientId)
            preferences[BASKET_INGREDIENTS_KEY] = currentFavorites.joinToString(",")
        }
    }

    fun getDislikeIngredients(): Flow<List<String>>{
        return dataStore.data.map { preferences ->
            preferences[DISLIKE_INGREDIENTS_KEY]?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        }
    }
    suspend fun addDislikeIngredient(ingredientId: String) {
        dataStore.edit {preferences ->
            val currentDislikes = preferences[DISLIKE_INGREDIENTS_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentDislikes.add(ingredientId)
            preferences[DISLIKE_INGREDIENTS_KEY] = currentDislikes.joinToString(",")
        }
    }
    suspend fun removeDislikeIngredient(ingredientId: String) {
        dataStore.edit {preferences ->
            val currentDislikes = preferences[DISLIKE_INGREDIENTS_KEY]?.split(",")?.toMutableList() ?: mutableListOf()
            currentDislikes.remove(ingredientId)
            preferences[DISLIKE_INGREDIENTS_KEY] = currentDislikes.joinToString(",")
        }
    }

    // RESET
    suspend fun resetFavouriteRecipes() {
        dataStore.edit { preferences ->
            preferences[FAVORITE_RECIPES_KEY] = ""
        }
    }

    suspend fun resetBasketIngredients() {
        dataStore.edit { preferences ->
            preferences[BASKET_INGREDIENTS_KEY] = ""
            preferences[DISLIKE_INGREDIENTS_KEY] = ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val UID_KEY = stringPreferencesKey("uid")
        private val LOGIN_KEY = booleanPreferencesKey("isLoggedIn")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val AGE_KEY = stringPreferencesKey("age")
        private val WEIGHT_KEY = stringPreferencesKey("weight")
        private val HEIGHT_KEY = stringPreferencesKey("height")
        private val FAVORITE_RECIPES_KEY = stringPreferencesKey("favorite_recipes")
        private val BASKET_INGREDIENTS_KEY = stringPreferencesKey("cart_ingredients")
        private val DISLIKE_INGREDIENTS_KEY = stringPreferencesKey("dislike_ingredients")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}