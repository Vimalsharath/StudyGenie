package com.studygenie.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studygenie.app.data.local.AppDatabase
import com.studygenie.app.data.local.UserEntity
import com.studygenie.app.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AuthRepository

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    val localUser: StateFlow<UserEntity?> = flow {
        val user = repository.currentUser
        if (user != null) {
            emitAll(repository.getLocalUser(user.uid))
        } else {
            emit(null)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = AuthRepository(userDao)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            result.onSuccess {
                _authState.value = AuthState.Success(it)
            }.onFailure { exception ->
                val errorMessage = if (exception.message?.contains("CONFIGURATION_NOT_FOUND") == true) {
                    "Auth Provider not enabled. Please enable Email/Password in Firebase Console."
                } else {
                    exception.message ?: "Login failed"
                }
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.register(email, password, name)
            result.onSuccess {
                _authState.value = AuthState.Success(it)
            }.onFailure { exception ->
                val errorMessage = if (exception.message?.contains("CONFIGURATION_NOT_FOUND") == true) {
                    "Auth Provider not enabled. Please enable Email/Password in Firebase Console."
                } else {
                    exception.message ?: "Registration failed"
                }
                _authState.value = AuthState.Error(errorMessage)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }

    fun updateProfile(name: String) {
        viewModelScope.launch {
            repository.updateProfile(name)
        }
    }

    fun changePassword(password: String) {
        viewModelScope.launch {
            repository.changePassword(password)
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
