package com.example.jetpackcomposeuserlist.ui.state

import com.example.jetpackcomposeuserlist.data.User

sealed class UserListUiState {

    data class Success(val data: List<User>) : UserListUiState()
    data class Error(val message: Int) : UserListUiState()
    object Loading : UserListUiState()
    object Empty : UserListUiState()

}