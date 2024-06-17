package com.dicoding.picodiploma.loginwithanimation.view.extrainfo

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.ExtraRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserProfile
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ProfileResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.UpdateResponse
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExtraViewModel(private val repository: ExtraRepository): ViewModel() {

    private val _profileResult = MutableLiveData<ProfileResponse?>()
    val profileResult: MutableLiveData<ProfileResponse?> get() = _profileResult

    private val _updateResult = MutableLiveData<UpdateResponse?>()
    val updateResult: MutableLiveData<UpdateResponse?> get() = _updateResult

    val session = repository.getSession().asLiveData()

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun getUid(): LiveData<String> {
        return repository.getUid().asLiveData()
    }
    fun saveUserProfile(user: UserProfile){
        viewModelScope.launch {
            repository.saveUserProfile(user)
        }
    }
    fun resetResult(){
        _updateResult.value = null
    }
    fun getUserProfile(uid: String){
        repository.getUserProfile(uid).enqueue(object: Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if(response.isSuccessful && response.body() != null){
                    val profileResponse = response.body()
                    if (profileResponse != null) {
                        saveUserProfile(profileResponse.userProfile!!)
                        _profileResult.value = profileResponse
                        println("Saved profileResult")
                    }
                } else {
                    //TODO Error
                    println("Response body error")
                    val nullProfile = ProfileResponse(UserProfile("", "", "", ""))
                    _profileResult.value = nullProfile
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    fun updateUserProfile(username: String, age: String, weight: String, height: String){
        repository.updateUserProfile(username, age, weight, height).enqueue(object: Callback<UpdateResponse>{
            override fun onResponse(
                call: Call<UpdateResponse>,
                response: Response<UpdateResponse>
            ) {
                if(response.isSuccessful && response.body() != null){
                    val updateResponse = response.body()
                    if (updateResponse != null) {
                        saveUserProfile(UserProfile(username, age, weight, height))
                        _updateResult.value = updateResponse
                        println("Update saved successfully!")
                    }
                } else {
                    //TODO Error
                    println("Error response body")
                    val jsonInString = response.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                    val message = errorBody.error
                    println("Error: $message")

                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}