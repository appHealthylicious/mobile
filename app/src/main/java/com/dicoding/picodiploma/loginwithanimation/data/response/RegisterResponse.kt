package com.dicoding.picodiploma.loginwithanimation.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
