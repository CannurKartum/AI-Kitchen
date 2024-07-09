package com.example.mythesisapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomNavItem(

    val icon: ImageVector,
    val route: String
){
    object MainPage : BottomNavItem(
        icon = Icons.Filled.Home,
        route = HomeRoutes.Main.name
    )
    object MealPlanner : BottomNavItem(
        icon = Icons.Filled.CalendarToday,
        route = HomeRoutes.MealPlanner.name
    )
    object ShoppingList : BottomNavItem(
        icon = Icons.Filled.FormatListNumbered,
        route = HomeRoutes.ShoppingList.name
    )
    object SavedRecipes : BottomNavItem(
        icon = Icons.Filled.Bookmark,
        route = HomeRoutes.SavedRecipes.name
    )
}
