package com.example.mythesisapp.ui.savedrecipes


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mythesisapp.data.model.Recipe

@Composable
fun SavedRecipes(
    viewModel: SavedRecipesViewModel = hiltViewModel(),
    onAddClick: () -> Unit,
    onRecipeClick: (String) -> Unit
) {
    val state = viewModel.uiState.collectAsState()

    Divider(thickness = 1.dp, color = Color.Black)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(8.dp)) {
            OutlinedTextField(
                value = viewModel.searchText.value,
                onValueChange = {
                    viewModel.onSearchQueryChanged(it)
                },
                label = { Text("Search") },
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                    }
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (state.value.filteredRecipes.isNotEmpty()) {
                LazyColumn {
                    items(state.value.filteredRecipes) { recipe ->
                        RecipeItem(
                            recipe = recipe,
                            onRecipeClick = onRecipeClick,
                            onDelete = { viewModel.deleteRecipe(it) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Recipe")
        }
    }
}

@Composable
fun RecipeItem(
    recipe: Recipe,
    onRecipeClick: (String) -> Unit,
    onDelete: (Recipe) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onRecipeClick(recipe.id)
            },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Recipe Name: ${recipe.title}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Ingredients: ${recipe.ingredient}")
            Text(text = "Description: ${recipe.description}")
            Text(text = "Cook Time: ${recipe.cooktime}")
            Spacer(modifier = Modifier.padding(5.dp))
            Row(modifier = Modifier.align(Alignment.Start)) {
                Button(onClick = { onDelete(recipe) }) {
                    Text("Delete")
                    Icon(
                        modifier = Modifier.padding(start = 5.dp),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}

