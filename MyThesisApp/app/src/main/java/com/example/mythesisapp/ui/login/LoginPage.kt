package com.example.mythesisapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mythesisapp.R

@Composable
fun LoginPage(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavToHomePage: () -> Unit,
    onNavToSingUpPage: () -> Unit,
) {
    val loginUiState = loginViewModel.loginUiState
    val context = LocalContext.current

    if (loginUiState.isSuccessLogin) {
        onNavToHomePage.invoke()
    }

    Box() {
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
                .padding(top = 130.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (loginUiState.loginError != null) {
                Toast.makeText(context, loginUiState.loginError, Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email input field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                value = loginUiState.email,
                onValueChange = { loginViewModel.onEmailChangeSignIn(it) },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = ""
                    )
                },
                isError = loginUiState.loginError != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.padding(5.dp))

            // Password input field
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp),
                value = loginUiState.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = ""
                    )
                },
                isError = loginUiState.loginError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            // Login button
            Button(
                onClick = { loginViewModel.loginUser() },
                modifier = Modifier
                    .size(width = 150.dp, height = 41.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.padding(10.dp))


            TextButton(onClick = { onNavToSingUpPage.invoke() }) {
                Text(text = " No Account? Create One", modifier = Modifier
                    .padding(10.dp))
            }


            if (loginUiState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}