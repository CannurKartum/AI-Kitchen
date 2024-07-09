package com.example.mythesisapp.ui.savedrecipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mythesisapp.data.model.Recipe

@Composable
fun AddRecipe(
    savedRecipesViewModel: SavedRecipesViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state = savedRecipesViewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var ingredient by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var cookTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Recipe Title:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        Text(text = "Ingredients for Recipe:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = ingredient,
            onValueChange = { ingredient = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Ingredients") }
        )
        Text(text = "Recipe Description:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Description") }
        )
        Text(text = "Cooking Time for your Recipe:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = cookTime,
            onValueChange = { cookTime = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Cook Time") }
        )
        Button(
            onClick = {
                savedRecipesViewModel.addRecipe(Recipe(title, ingredient, description, cookTime))
                navigateBack()
            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Add Recipe")
        }
    }
}