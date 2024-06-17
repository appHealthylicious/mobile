package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.api.Api
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserProfile
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ProfileResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class UserRepository private constructor(
    private val apiService: Api,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }
    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email,password)
    }
    fun register(email: String, password: String): Call<RegisterResponse>{
        return apiService.register(email, password)
    }

    companion object {
        fun getInstance(
            apiService: Api,
            userPreference: UserPreference
        ) = UserRepository(apiService, userPreference)
    }

}