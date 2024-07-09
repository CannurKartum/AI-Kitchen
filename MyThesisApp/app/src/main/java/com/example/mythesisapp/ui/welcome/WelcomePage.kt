package com.example.mythesisapp.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mythesisapp.R
import com.example.mythesisapp.ui.login.LoginViewModel


@Composable
fun WelcomePage(loginViewModel: LoginViewModel? = null,
                onNavToSingUpPage:() -> Unit,
                onNavToLoginPage:() -> Unit,
                ){

Box {

    Image(painter = painterResource(id = R.drawable.welcomepage),
        contentDescription = "Welcome-Page",
        modifier = Modifier.fillMaxSize().padding(bottom = 50.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = 155.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome to AI Kitchen", fontSize = 32.sp)
    }
    Row(modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 70.dp)) {

        Button(onClick = { onNavToSingUpPage.invoke() }, Modifier.size(width = 150.dp, height = 41.dp)) {
            Text(text = "Create Account")
        }
        Spacer(modifier = Modifier.padding(20.dp))

        ElevatedButton(onClick = { onNavToLoginPage.invoke() }, Modifier.size(width = 150.dp, height = 41.dp)) {
            Text(text = "Login")
        }
    }

}


}
