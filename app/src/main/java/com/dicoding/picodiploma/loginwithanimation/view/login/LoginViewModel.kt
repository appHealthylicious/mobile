package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserProfile
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.ProfileResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: MutableLiveData<LoginResponse?> get() = _loginResult

    private val _profileResult = MutableLiveData<ProfileResponse?>()
    val profileResult: MutableLiveData<ProfileResponse?> get() = _profileResult


    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun login(email: String, password: String){
        repository.login(email, password).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if(response.isSuccessful && response.body() != null){
                    println("Responsebody is successful")
                    val loginResponse = response.body()
                    saveSession(UserModel(
                        loginResponse?.uid.toString(),
                        email,
                        loginResponse?.token.toString(),
                        true,
                        UserProfile("", "", "", "")
                        ))
                    _loginResult.value = loginResponse
                    println("Saved ${loginResponse}")
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        var msg: String = ""
                        if (errorBody != null) {
                            val errorResponse = JSONObject(errorBody)
                            val errorMessage = errorResponse.getString("error")
                            println("Error: $errorMessage")
                            if(
                                errorMessage == "INVALID_EMAIL"
                            ){
                                msg = "Email invalid."
                            } else if(
                                errorMessage == "INVALID_LOGIN_CREDENTIALS"
                            ) {
                                msg = "Password invalid"
                            }
                        }
                        _loginResult.value = LoginResponse("", msg,"",true)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}