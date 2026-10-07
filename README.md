# Jetpack Compose User List

A single-screen Android app that fetches GitHub users from `https://api.github.com/users` and shows them in a list. Built to demonstrate a clean MVVM setup with Jetpack Compose, Hilt, Retrofit, Kotlin Flow and coroutines.

## Features

- Loads GitHub users (avatar and username) in a `LazyColumn`
- Four UI states: **Loading**, **Success**, **Empty** and **Error**
- Network failures are handled without crashing the app
- Error messages come from `strings.xml`, so they can be translated

## Tech stack

| Area | Library |
|---|---|
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, unidirectional data flow (`StateFlow`) |
| Dependency injection | Hilt |
| Networking | Retrofit, Gson converter |
| Async | Kotlin coroutines, Flow |
| Image loading | Coil 3 |

## Architecture

```
UserListApi (Retrofit, suspend fun)
   -> UserRepository   flow { emit(api.getUserList()) }.flowOn(Dispatchers.IO)
   -> UserListViewModel   collects the Flow, exposes StateFlow<UserListUiState>
   -> UserListScreen   renders the current state
```

The repository returns plain data (`Flow<List<User>>`). The ViewModel turns it into UI state and maps exceptions to user-facing messages.

### UI states

| State | When |
|---|---|
| `Loading` | Initial state, while the request is running |
| `Success` | The API returned at least one user |
| `Empty` | The API returned an empty list |
| `Error` | The request failed (see below) |

### Error handling

Exceptions from the Flow are caught in the ViewModel with `.catch` and mapped to string resources:

| Exception | Message resource |
|---|---|
| `IOException` (no connection, timeout) | `error_no_internet` |
| `HttpException` with code 403 (GitHub rate limit) | `error_rate_limit` |
| Any other `HttpException` | `error_server` |
| Anything else | `error_generic` |

## Project structure

```
com.example.jetpackcomposeuserlist
├── data
│   ├── User.kt
│   └── UserRepository.kt
├── network
│   └── UserListApi.kt
├── di
│   └── NetworkModule.kt           Retrofit and API provided by Hilt
├── ui
│   ├── UserListScreen.kt
│   ├── UserListViewModel.kt
│   ├── state
│   │   └── UserListUiState.kt
│   └── theme
└── MainActivity.kt
```

## Getting started

1. Clone the repository and open it in Android Studio.
2. Let Gradle sync finish.
3. Run the `app` configuration on an emulator or device (minSdk 24).

The app needs an internet connection and the `INTERNET` permission in `AndroidManifest.xml`.

## Notes

- The GitHub API allows about 60 unauthenticated requests per hour per IP address. If you hit the limit, the app shows the rate-limit message.
- `strings.xml` holds all error texts. Add `values-<lang>/strings.xml` to localize them.