package com.studygenie.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studygenie.app.data.local.AppDatabase
import com.studygenie.app.data.local.UserEntity
import com.studygenie.app.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: AuthRepository by lazy {
        val userDao = AppDatabase.getDatabase(application).userDao()
        AuthRepository(userDao)
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    val localUser: StateFlow<UserEntity?> by lazy {
        flow {
            val user = repository.currentUser
            if (user != null) {
                emitAll(repository.getLocalUser(user.uid))
            } else {
                emit(null)
            }
        }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope, 
            started = SharingStarted.WhileSubscribed(5000), 
            initialValue = null
        )
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            result.onSuccess { _authState.value = AuthState.Success(it) }
            .onFailure { _authState.value = AuthState.Error(it.message ?: "Login failed") }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            val result = repository.register(email, password, name)
            result.onSuccess { _authState.value = AuthState.Success(it) }
            .onFailure { _authState.value = AuthState.Error(it.message ?: "Registration failed") }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }

    fun updateProfile(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(name)
        }
    }

    fun changePassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changePassword(password)
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
