package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.api.Api
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserProfile
import com.dicoding.picodiploma.loginwithanimation.data.response.ProfileResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.UpdateResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class ExtraRepository private constructor (
    val apiService: Api,
    private val userPreference: UserPreference
){
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }
    fun getUserProfile(uid: String): Call<ProfileResponse> {
        return apiService.getUserProfile(uid)
    }
    fun updateUserProfile(username: String, age: String, weight: String, height: String): Call<UpdateResponse>{
        return apiService.updateUserProfile(username, age, weight, height)
    }
    suspend fun saveUserProfile(user: UserProfile){
        userPreference.saveUserProfile(user)
    }

    fun getUid(): Flow<String> {
        return userPreference.getUid()
    }
    fun getSession(): Flow<UserModel>{
        return userPreference.getSession()
    }
    companion object {
        fun getInstance(
            apiService: Api,
            userPref: UserPreference
        ) = ExtraRepository(apiService, userPref)
    }
}