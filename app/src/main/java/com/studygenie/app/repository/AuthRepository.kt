package com.studygenie.app.repository

import com.studygenie.app.data.local.UserDao
import com.studygenie.app.data.local.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AuthRepository(private val userDao: UserDao) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    fun getLocalUser(userId: String): Flow<UserEntity?> = userDao.getUser(userId)

    suspend fun updateUserStats(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                // Sync Firebase name to local database for offline access
                val userEntity = UserEntity(
                    id = user.uid,
                    name = user.displayName ?: "User",
                    email = user.email ?: email
                )
                userDao.insertUser(userEntity)
                Result.success(user)
            } ?: Result.failure(Exception("Login failed: User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { user ->
                // 1. Update Firebase Profile Name
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user.updateProfile(profileUpdates).await()

                // 2. Save to local Room database
                val userEntity = UserEntity(
                    id = user.uid,
                    name = name,
                    email = email
                )
                userDao.insertUser(userEntity)
                Result.success(user)
            } ?: Result.failure(Exception("Registration failed: User is null"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        firebaseAuth.signOut()
        userDao.clearAll()
    }

    suspend fun updateProfile(name: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: return Result.failure(Exception("Not logged in"))
            
            // 1. Update Firebase
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            user.updateProfile(profileUpdates).await()

            // 2. Update Local
            val localUser = userDao.getUserSync(user.uid)
            if (localUser != null) {
                userDao.insertUser(localUser.copy(name = name))
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(newPassword: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser ?: return Result.failure(Exception("Not logged in"))
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
