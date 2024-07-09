package com.example.mythesisapp.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mythesisapp.R
import com.example.mythesisapp.ui.navigation.LoginRoutes


@Composable
fun ProfilePage(profilePageViewModel: ProfilePageViewModel = hiltViewModel(),
                navController: NavHostController, )
{

    val state = profilePageViewModel.profileUiState

    Column(modifier = Modifier
        .padding(10.dp)
        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Divider(thickness = 1.dp, color = Color.Black)

        Spacer(modifier = Modifier.padding(5.dp))

        Image(
            painter = painterResource(id = R.drawable.person),
            contentDescription = "Profile Page",
            modifier = Modifier
                .size(300.dp)
                .padding(top = 30.dp, bottom = 50.dp)
        )

        Spacer(modifier = Modifier.padding(10.dp))


        var isEditing by remember { mutableStateOf(false) }
        var emailInput by remember { mutableStateOf(state.email) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (!isEditing) {
                Text(text = "User Email: ${state.email}")
                IconButton(onClick = {
                    isEditing = true
                    emailInput = state.email
                }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Email")
                }
            } else {
                TextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Edit Email") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        profilePageViewModel.changeUserEmail(emailInput)
                        isEditing = false
                    }),
                    singleLine = true
                )
            }
        }

        if (state.updateEmailSuccess) {
            Text("Email updated successfully!", color = Color.Green)
        }

        if (state.updateEmailError != null) {
            Text("Error: ${state.updateEmailError}", color = Color.Red)
        }


        ElevatedButton(
            onClick = {
                profilePageViewModel.logout()
                navController.navigate(LoginRoutes.SignIn.name)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Logout")
        }

        if (profilePageViewModel.profileUiState.requiresReauth) {
            ReauthenticationDialog(onConfirm = { email, password ->
                profilePageViewModel.reauthenticate(email, password)
            })
        }

    }
}


@Composable
fun ReauthenticationDialog(onConfirm: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Re-authenticate") },
        text = {
            Column {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(email, password) }) {
                Text("Confirm")
            }
        }
    )
}