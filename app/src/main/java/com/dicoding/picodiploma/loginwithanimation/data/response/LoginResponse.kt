package com.dicoding.picodiploma.loginwithanimation.data.response

data class LoginResponse(
	val uid: String? = null,
	val message: String? = null,
	val token: String? = null,
	val error: Boolean? = false
)

