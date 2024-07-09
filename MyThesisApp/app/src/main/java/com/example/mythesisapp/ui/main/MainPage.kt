package com.example.mythesisapp.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mythesisapp.R
import com.example.mythesisapp.ui.login.LoginViewModel
import com.example.mythesisapp.ui.navigation.HomeRoutes
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(loginViewModel: LoginViewModel = hiltViewModel(),
             navController: NavHostController,
             onNavToProfile: () -> Unit,
             onNavToMealPlanner:() -> Unit){

    Column(modifier = Modifier
        .padding(10.dp)
        .fillMaxSize()) {
        Divider( thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            modifier = Modifier.padding(8.dp),
            text = "Hi User! You can start to manage your meals and enjoy cooking!"
        )

        Spacer(modifier = Modifier.padding(50.dp))

        Text(
            modifier = Modifier.padding(8.dp),
            text = "Let’s talk about what we going to eat today? "
        )

        ElevatedButton(onClick = { navController.navigate(HomeRoutes.AIChat.name)},
            border = BorderStroke(2.dp, Color((0xFF4E34E2))),
            modifier = Modifier.padding(start = 20.dp)) {
            Text(modifier = Modifier.padding(10.dp),text = "Start creating recipe with AI Chat!")
        }
        Image(
            painter = painterResource(id = R.drawable.chefmain),
            contentDescription = "Chef-image",
            modifier = Modifier
                .size(250.dp)
                .padding(top = 30.dp, bottom = 30.dp)
                .align(Alignment.End)

        )

    }
}


