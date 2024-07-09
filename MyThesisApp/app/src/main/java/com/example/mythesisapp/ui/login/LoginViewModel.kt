package com.example.mythesisapp.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.data.repository.AuthRepository
import com.example.mythesisapp.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var loginUiState by mutableStateOf(LoginUiState())
        private set


    fun onEmailChangeSignIn(email: String) {
        loginUiState = loginUiState.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        loginUiState = loginUiState.copy(password = password)
    }

    fun onEmailChangeSignUp(emailSignUp: String) {
        loginUiState = loginUiState.copy(emailSignUp = emailSignUp)
    }

    fun onPasswordChangeSignUp(passwordSignUp: String) {
        loginUiState = loginUiState.copy(passwordSignUp = passwordSignUp)
    }

    fun onConfirmPassword(confirmPasswordSignUp: String) {
        loginUiState = loginUiState.copy(confirmPasswordSignUp = confirmPasswordSignUp)
    }

    private fun validateLoginForm() =
        loginUiState.email.isNotBlank() &&
                loginUiState.password.isNotBlank()

    private fun validateSignUpForm() =
        loginUiState.emailSignUp.isNotBlank() &&
                loginUiState.passwordSignUp.isNotBlank() &&
                loginUiState.confirmPasswordSignUp.isNotBlank()


    fun createUser() = viewModelScope.launch {
        loginUiState = loginUiState.copy(isLoading = true)
        if (!validateSignUpForm()) {
            loginUiState = loginUiState.copy(signUpError = "Email and password cannot be empty", isLoading = false)
            return@launch
        }

        if (loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp) {
            loginUiState = loginUiState.copy(signUpError = "Password do not match", isLoading = false)
            return@launch
        }

        loginUiState = when (val result = repository.createUser(loginUiState.emailSignUp, loginUiState.passwordSignUp)) {
            is Resource.Success -> {
                loginUiState.copy(isSuccessLogin = true)
            }

            is Resource.Error -> {
                loginUiState.copy(
                    isSuccessLogin = false,
                    signUpError = result.errorMessage,
                    isLoading = false
                )
            }
        }
    }

    fun loginUser() = viewModelScope.launch {
        loginUiState = loginUiState.copy(isLoading = true)
        if (!validateLoginForm()) {
            loginUiState = loginUiState.copy(loginError = "Email and password cannot be empty", isLoading = false)
            return@launch
        }

        loginUiState = when (val result = repository.login(loginUiState.email, loginUiState.password)) {
            is Resource.Success -> {
                loginUiState.copy(isSuccessLogin = true)
            }

            is Resource.Error -> {
                loginUiState.copy(
                    isSuccessLogin = false,
                    loginError = result.errorMessage,
                    isLoading = false
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val signUpError: String? = null,
    val loginError: String? = null
)