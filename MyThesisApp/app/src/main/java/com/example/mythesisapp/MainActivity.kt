package com.example.mythesisapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mythesisapp.ui.navigation.BottomNavItem
import com.example.mythesisapp.ui.navigation.HomeRoutes
import com.example.mythesisapp.ui.navigation.LoginRoutes
import com.example.mythesisapp.ui.navigation.Navigation
import com.example.mythesisapp.ui.theme.BackGroundColor
import com.example.mythesisapp.ui.theme.MyThesisAppTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyThesisAppTheme {
                Surface(Modifier.fillMaxSize(), color = BackGroundColor) {

                    var isLoggedIn by remember { mutableStateOf(firebaseAuth.currentUser != null) }
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()

                    // Observe FirebaseAuth state changes
                    LaunchedEffect(key1 = firebaseAuth) {
                        firebaseAuth.addAuthStateListener { auth ->
                            isLoggedIn = auth.currentUser != null
                        }

                    }

                    var title by remember { mutableStateOf("My Thesis App") }
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // Conditional Scaffold based on login status
                    if (isLoggedIn) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text(text = title, style = MaterialTheme.typography.headlineMedium) },
                                    actions = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                navController.navigate(HomeRoutes.Profile.name)
                                            }
                                        }) {
                                            Icon(
                                                modifier = Modifier.fillMaxHeight().padding(end = 10.dp),
                                                imageVector = Icons.Filled.Person,
                                                contentDescription = "Profile"
                                            )
                                        }
                                    },
                                )
                            },
                            bottomBar = { BottomAppBar(navController = navController) },
                        ) { contentPadding ->
                            Navigation(
                                modifier = Modifier.padding(contentPadding),
                                navController = navController,
                                startDestination = HomeRoutes.Main.name,
                                screenName = { title = it }
                            )
                        }
                    } else {
                        // Show only login or welcome screen when not logged in
                        Navigation(
                            modifier = Modifier.fillMaxSize(),
                            navController = navController,
                            startDestination = LoginRoutes.Welcome.name,
                            screenName = { title = it }
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun BottomAppBar(navController: NavHostController){
   val navItem = listOf(
       BottomNavItem.MainPage,
       BottomNavItem.MealPlanner,
       BottomNavItem.ShoppingList,
       BottomNavItem.SavedRecipes
   )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar() {
        NavigationBar {
            navItem.forEach { navItem ->
                AddItem(navItem = navItem,
                    currentDestination = currentDestination,
                    navController = navController)
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    navItem: BottomNavItem,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    NavigationBarItem(
        selected = currentDestination?.hierarchy?.any {
            it.route == navItem.route
        } == true,
        onClick = {
            navController.navigate(navItem.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        icon = {
        Icon(
            imageVector = navItem.icon,
            contentDescription = "Navigation Icon"
        )
    },)
}