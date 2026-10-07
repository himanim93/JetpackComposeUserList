package com.example.jetpackcomposeuserlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcomposeuserlist.R
import com.example.jetpackcomposeuserlist.data.UserRepository
import com.example.jetpackcomposeuserlist.ui.state.UserListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUsers()
                .catch { _uiState.value = UserListUiState.Error(message = it.toUserMessage()) }
                .collect {
                    if (it.isEmpty()) {
                        _uiState.value = UserListUiState.Empty
                    } else {
                        _uiState.value = UserListUiState.Success(data = it)
                    }
                }
        }
    }

    private fun Throwable.toUserMessage(): Int = when (this) {
        is IOException -> R.string.error_no_internet
        is HttpException ->
            if (code() == 403) R.string.error_rate_limit
            else R.string.error_server

        else -> R.string.error_generic
    }
}