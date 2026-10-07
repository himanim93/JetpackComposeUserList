package com.example.jetpackcomposeuserlist.data

import com.example.jetpackcomposeuserlist.network.UserListApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(private val userListApi: UserListApi) {

    fun getUsers() : Flow<List<User>> = flow {
        emit(userListApi.getUserList())
    }.flowOn(Dispatchers.IO)

}