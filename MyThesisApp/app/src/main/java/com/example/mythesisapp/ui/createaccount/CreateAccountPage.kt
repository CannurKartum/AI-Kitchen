package com.example.mythesisapp.ui.createaccount

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mythesisapp.R
import com.example.mythesisapp.ui.login.LoginViewModel

@Composable
fun CreateAccountPage(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavToHomePage: () -> Unit,
    onNavToLoginPage: () -> Unit,
) {
    val loginUiState = loginViewModel.loginUiState
    val isError = loginUiState.signUpError != null
    val context = LocalContext.current

    if (loginUiState.isSuccessLogin) {
        onNavToHomePage.invoke()
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.loginpage),
            contentDescription = "Login-Page",
            modifier = Modifier
                .size(300.dp)
                .padding(top = 30.dp, bottom = 50.dp)
                .align(Alignment.TopCenter)
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 230.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isError) {
                Text(text = loginUiState?.signUpError ?: "Unkown error",
                    color = Color.Red , modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally))
            }
            Spacer(modifier = Modifier.height(16.dp))


            // Email input field
            OutlinedTextField(
                value = loginUiState?.emailSignUp ?: "",
                onValueChange = { loginViewModel?.onEmailChangeSignUp(it) },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = ""
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )


            // Password input field
            OutlinedTextField(
                value = loginUiState?.passwordSignUp ?: "",
                onValueChange = { loginViewModel?.onPasswordChangeSignUp(it) },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = ""
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )


            // Confirm Password input field
            OutlinedTextField(
                value = loginUiState?.confirmPasswordSignUp ?: "",
                onValueChange = { loginViewModel?.onConfirmPassword(it) },
                label = { Text("Confirm Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = ""
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            // Login button
            Button(
                onClick = { loginViewModel.createUser() },
                modifier = Modifier
                    .size(width = 150.dp, height = 41.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Create Account")
            }

            Row {
                TextButton(onClick = { onNavToLoginPage.invoke() }) {
                    Text(text = "Already have an account? Sign in",
                        modifier = Modifier.padding(start = 20.dp))
                }
            }
        }
    }
}