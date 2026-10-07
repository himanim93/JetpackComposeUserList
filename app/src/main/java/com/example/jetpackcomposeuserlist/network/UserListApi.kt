package com.example.jetpackcomposeuserlist.network

import com.example.jetpackcomposeuserlist.data.User
import retrofit2.http.GET

interface UserListApi {

    @GET("users")
    suspend fun getUserList(): List<User>
}