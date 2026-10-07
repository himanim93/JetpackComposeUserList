package com.example.jetpackcomposeuserlist.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.jetpackcomposeuserlist.R
import com.example.jetpackcomposeuserlist.data.User
import com.example.jetpackcomposeuserlist.ui.state.UserListUiState
import com.example.jetpackcomposeuserlist.ui.theme.Typography

@Composable
fun UserListScreen(viewModel: UserListViewModel = hiltViewModel(), modifier: Modifier) {
    val uiState = viewModel.uiState.collectAsState()
    Column(modifier = modifier.fillMaxSize()) {

        Text(
            text = stringResource(R.string.user_list_title),
            style = Typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        when (uiState.value) {
            UserListUiState.Loading -> {
                UserListLoading()
            }

            is UserListUiState.Success -> {
                UserList(data = (uiState.value as UserListUiState.Success).data)
            }

            is UserListUiState.Error -> {
                UserListError(message = stringResource((uiState.value as UserListUiState.Error).message))
            }

            UserListUiState.Empty -> {
                UserListError(message = stringResource(R.string.no_users_found))
            }
        }
    }
}

@Composable
fun UserListLoading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun UserList(data: List<User>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(data) { item ->
            UserItem(item)
        }
    }
}

@Composable
fun UserItem(item: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.avatarUrl,
            contentDescription = stringResource(R.string.image_content_description),
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = CircleShape
                ),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.login,
            style = Typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun UserListError(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = Typography.labelLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    UserList(
        data = listOf(
            User(
                1,
                "mojombo",
                "https://avatars.githubusercontent.com/"
            ), User(
                2,
                "mojombo",
                "https://avatars.githubusercontent.com/"
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun UserListScreenErrorPreview() {
    UserListError("Data Not Found")
}