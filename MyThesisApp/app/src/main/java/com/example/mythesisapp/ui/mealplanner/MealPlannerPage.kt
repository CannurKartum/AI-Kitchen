package com.example.mythesisapp.ui.mealplanner



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mythesisapp.data.model.Food
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color


@Composable
fun MealPlannerPage(
    viewModel: MealPlannerViewModel = hiltViewModel(),
    onNavToProfile: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        MealCard("Breakfast", viewModel.breakfastFoods.collectAsState(), viewModel, "breakfastfood")
        MealCard("Lunch", viewModel.lunchFoods.collectAsState(), viewModel, "lunchfood")
        MealCard("Dinner", viewModel.dinnerFoods.collectAsState(), viewModel, "dinnerfood")
    }

}

@Composable
fun MealCard(mealType: String, foodsState: State<List<Food>>, viewModel: MealPlannerViewModel, collectionName: String) {
    var showDialog by remember { mutableStateOf(false) }
    var textInput by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().padding(5.dp) .border(
                width = 2.dp,
                color = androidx.compose.ui.graphics.Color.Magenta,
                shape = RoundedCornerShape(10.dp)
            ).background(Color.White, shape = RoundedCornerShape(10.dp)), horizontalArrangement = Arrangement.SpaceBetween){
                Text(mealType, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp))
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Food")
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add Food to $mealType") },
                    text = {
                        TextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            label = { Text("Food Name") }
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            if (textInput.isNotEmpty()) {
                                viewModel.addFood(collectionName, textInput)
                                textInput = ""
                                showDialog = false
                            }
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }


            LazyColumn {
                items(foodsState.value) { food ->
                    FoodItem(food, viewModel, collectionName)
                }
            }
        }
    }
}

@Composable
fun FoodItem(food: Food, viewModel: MealPlannerViewModel, collectionName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(food.foodName, style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = { viewModel.deleteFood(collectionName, food.foodName) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Food")
        }
    }
}