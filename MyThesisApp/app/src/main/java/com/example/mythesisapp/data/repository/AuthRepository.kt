package com.example.mythesisapp.data.repository

import android.util.Log
import com.example.mythesisapp.common.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
) {

    fun getUserEmail() = firebaseAuth.currentUser?.email.orEmpty()

    suspend fun createUser(
        email: String,
        password: String,
    ): Resource<Unit> {
        var result: Resource<Unit> = Resource.Error("An unknown error occurred")
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        result = if (it.isSuccessful) {
                            Resource.Success(Unit)
                        } else {
                            Resource.Error(it.exception?.message.orEmpty())
                        }
                    }.await()
            } catch (e: Exception) {
                result = Resource.Error(e.message.orEmpty())
            }
        }
        return result
    }

    suspend fun login(
        email: String,
        password: String,
    ): Resource<Unit> {
        var result: Resource<Unit> = Resource.Error("An unknown error occurred")
        withContext(Dispatchers.IO) {
            try {
                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        result = if (it.isSuccessful) {
                            Resource.Success(Unit)
                        } else {
                            Resource.Error(it.exception?.message.orEmpty())
                        }
                    }.await()
            } catch (e: Exception) {
                result = Resource.Error(e.message.orEmpty())
            }
        }
        return result
    }

    fun reauthenticate(email: String, password: String): Resource<Unit> {
        val credential = EmailAuthProvider.getCredential(email, password)
        return firebaseAuth.currentUser?.let { user ->
            var result: Resource<Unit> = Resource.Error("Re-authentication failed")
            user.reauthenticate(credential).addOnCompleteListener { task ->
                result = if (task.isSuccessful) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error(task.exception?.message ?: "Re-authentication failed")
                }
            }
            result
        } ?: Resource.Error("No user logged in")
    }

    fun updateUserEmail(newEmail: String): Resource<Unit> {
        var result: Resource<Unit> = Resource.Error("Failed to update email")
        firebaseAuth.currentUser?.let { user ->
            user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener { task ->
                result = if (task.isSuccessful) {
                    Resource.Success(Unit)
                } else {
                    val errorDetail = task.exception?.message ?: "Email update failed"
                    Log.e("Auth", "Failed to update email: $errorDetail")
                    Resource.Error(errorDetail)
                }
            }
        } ?: run {
            Log.e("Auth", "No authenticated user found.")
            return Resource.Error("No authenticated user found.")
        }
        return result
    }
    fun logout(): Resource<Unit> {
        firebaseAuth.signOut()
        return if (firebaseAuth.currentUser == null) {
            Resource.Success(Unit) // Logout successful
        } else {
            Resource.Error("Logout failed") // Logout failed
        }
    }
}