package com.example.mythesisapp.ui.recipedetail

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecipeDetailScreen(
    navigateBack: () -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState.collectAsState()

    state.value.errorMessage?.let {
        Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Recipe Title:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = state.value.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onValueChange = {
                viewModel.onTitleChange(it)
            },
            label = { Text("Title") }
        )
        Text(text = "Ingredients for Recipe:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = state.value.ingredient,
            onValueChange = {
                viewModel.onIngredientChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Ingredients") }
        )
        Text(text = "Recipe Description:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = state.value.description,
            onValueChange = {
                viewModel.onDescriptionChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Description") }
        )
        Text(text = "Cooking Time for your Recipe:", modifier = Modifier.padding(10.dp))
        OutlinedTextField(
            value = state.value.cookTime,
            onValueChange = {
                viewModel.onCookTimeChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text("Cook Time") }
        )
        Button(
            onClick = {
                viewModel.updateRecipe()
                navigateBack()
            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Update Recipe")
        }
    }
}