package com.example.mythesisapp.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.common.Resource
import com.example.mythesisapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePageViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var profileUiState by mutableStateOf(ProfileUiState())
        private set

    init {
        fetchUserEmail()
    }

    private fun fetchUserEmail() {
        profileUiState = profileUiState.copy(email = repository.getUserEmail())
    }

    fun changeUserEmail(newEmail: String) = viewModelScope.launch {
        profileUiState = profileUiState.copy(isLoading = true)
        val result = repository.updateUserEmail(newEmail)
        when (result) {
            is Resource.Success -> {
                profileUiState = profileUiState.copy(
                    isLoading = false,
                    updateEmailSuccess = true,
                    email = newEmail
                )
            }
            is Resource.Error -> {
                if (result.errorMessage.contains("requires recent authentication")) {
                    profileUiState = profileUiState.copy(
                        isLoading = false,
                        requiresReauth = true,
                        pendingNewEmail = newEmail
                    )
                } else {
                    profileUiState = profileUiState.copy(
                        isLoading = false,
                        updateEmailError = result.errorMessage
                    )
                }
            }
        }
    }

    fun reauthenticate(email: String, password: String) = viewModelScope.launch {
        val reauthResult = repository.reauthenticate(email, password)
        if (reauthResult is Resource.Success) {
            changeUserEmail(profileUiState.pendingNewEmail)
            profileUiState = profileUiState.copy(requiresReauth = false)
        } else {
            profileUiState = profileUiState.copy(
                reauthError = (reauthResult as Resource.Error).errorMessage,
                requiresReauth = false
            )
        }
    }

    fun logout(){
        repository.logout()
    }
}


data class ProfileUiState(
    val email: String = "",
    val pendingNewEmail: String = "",
    val isLoading: Boolean = false,
    val updateEmailSuccess: Boolean = false,
    val updateEmailError: String? = null,
    val reauthError: String? = null,
    val requiresReauth: Boolean = false
)