package com.example.jetpackcomposeuserlist.data

import com.google.gson.annotations.SerializedName

data class User(
    val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)