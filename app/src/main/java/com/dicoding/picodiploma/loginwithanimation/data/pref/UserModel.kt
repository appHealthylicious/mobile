package com.dicoding.picodiploma.loginwithanimation.data.pref

data class UserModel(
    val uid: String,
    val email: String,
    val token: String,
    val isLoggedIn: Boolean,
    val extraInfo: UserProfile
)

data class UserProfile(
    val username: String,
    val age: String,
    val weight: String, //in kg
    val height: String //in cm
)