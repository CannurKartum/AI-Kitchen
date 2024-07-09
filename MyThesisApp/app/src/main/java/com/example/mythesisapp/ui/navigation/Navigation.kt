package com.example.mythesisapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mythesisapp.ui.aichat.AiChat
import com.example.mythesisapp.ui.createaccount.CreateAccountPage
import com.example.mythesisapp.ui.login.LoginPage
import com.example.mythesisapp.ui.main.MainPage
import com.example.mythesisapp.ui.mealplanner.MealPlannerPage
import com.example.mythesisapp.ui.profile.ProfilePage
import com.example.mythesisapp.ui.recipedetail.RecipeDetailScreen
import com.example.mythesisapp.ui.savedrecipes.AddRecipe
import com.example.mythesisapp.ui.savedrecipes.SavedRecipes
import com.example.mythesisapp.ui.shoppinglist.ShoppingListPage
import com.example.mythesisapp.ui.shoppinglist.addshoppinglistitem.AddShoppingListItem
import com.example.mythesisapp.ui.welcome.WelcomePage


enum class LoginRoutes {
    Welcome,
    SignUp,
    SignIn
}

enum class HomeRoutes {
    Main,
    Profile,
    MealPlanner,
    ShoppingList,
    SavedRecipes,
    AddRecipe,
    RecipeDetail,
    AddProduct,
    AIChat
}


@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = LoginRoutes.Welcome.name,
    screenName: (String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(route = LoginRoutes.Welcome.name) {
            WelcomePage(onNavToSingUpPage = { navController.navigate(LoginRoutes.SignUp.name) }) {
                navController.navigate(LoginRoutes.SignIn.name) {}
            }
        }
        composable(route = LoginRoutes.SignIn.name) {
            LoginPage(
                onNavToHomePage = {
                    navController.navigate(HomeRoutes.Main.name) {
                        launchSingleTop = true
                        popUpTo(route = LoginRoutes.SignIn.name) {
                            inclusive = true
                        }
                    }
                }
            ) {
                navController.navigate(LoginRoutes.SignUp.name) {
                    launchSingleTop = true
                    popUpTo(LoginRoutes.SignIn.name) {
                        inclusive = true
                    }
                }

            }
        }
        composable(route = LoginRoutes.SignUp.name) {
            CreateAccountPage(
                onNavToHomePage = {
                    navController.navigate(HomeRoutes.Main.name) {
                        popUpTo(LoginRoutes.SignUp.name) {
                            inclusive = true
                        }
                    }
                }
            )
            {
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }
        composable(route = HomeRoutes.Profile.name) {
            screenName("Profile")
            ProfilePage(navController = navController)
        }
        composable(route = HomeRoutes.Main.name) {
            screenName("Welcome to AI Kitchen")
            MainPage(onNavToProfile = {
                navController.navigate(HomeRoutes.Profile.name)
            }, onNavToMealPlanner = {
                navController.navigate(HomeRoutes.MealPlanner.name)
            }, navController = navController)
        }
        composable(route = HomeRoutes.ShoppingList.name) {
            screenName("Shopping List")
            ShoppingListPage(navController = navController)
        }
        composable(route = HomeRoutes.MealPlanner.name) {
            screenName("Daily Meal Planner")
            MealPlannerPage(onNavToProfile = {
                navController.navigate(HomeRoutes.Profile.name)
            }
            )
        }
        composable(route = HomeRoutes.SavedRecipes.name) {
            screenName("Saved Recipes")
            SavedRecipes(
                onAddClick = {
                    navController.navigate(HomeRoutes.AddRecipe.name)
                },
                onRecipeClick = {
                    navController.navigate(HomeRoutes.RecipeDetail.name.plus("/$it"))
                }
            )
        }
        composable(route = HomeRoutes.AddRecipe.name) {
            screenName("Add Recipe")
            AddRecipe(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(route = HomeRoutes.AddProduct.name) {
            screenName("Add Item")
            AddShoppingListItem(onProductAdded = { navController.popBackStack() })
        }
        composable(route = HomeRoutes.AIChat.name) {
            screenName("AI Chat")
            AiChat(navController = navController)
        }
        composable(
            route = HomeRoutes.RecipeDetail.name.plus("/{id}"),
        ) {
            screenName("Recipe Detail")
            RecipeDetailScreen(
                navigateBack = { navController.navigateUp() },
            )
        }
    }
}















