package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private val _result = MutableLiveData<RegisterResponse?>()
    val result: MutableLiveData<RegisterResponse?> get() = _result

    fun resetRegister(){
        _result.value = null
    }

    fun register(email: String, password: String){
        repository.register(email, password).enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if(response.isSuccessful && response.body() != null) {
                    val registerResponse = response.body()
                    _result.value = registerResponse
                } else {
                    try {
                        var errorMessage: String? = null
                        val errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            val errorResponse = JSONObject(errorBody)
                            errorMessage = errorResponse.getString("error")
                            println("Error: $errorMessage")
                        }
                        _result.value = RegisterResponse("", errorMessage)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }}
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}