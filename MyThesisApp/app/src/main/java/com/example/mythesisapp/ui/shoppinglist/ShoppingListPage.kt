package com.example.mythesisapp.ui.shoppinglist



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.mythesisapp.R
import com.example.mythesisapp.data.model.Product
import com.example.mythesisapp.ui.shoppinglist.addshoppinglistitem.ShoppingListViewModel
import com.example.mythesisapp.ui.navigation.HomeRoutes


@Composable
fun ShoppingListPage(
    viewModel: ShoppingListViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state = viewModel.shoppingListUIState
    val productList = state.productList
    Divider( thickness = 1.dp, color = Color.Black)
    Spacer(modifier = Modifier.padding(5.dp))
    Box(modifier = Modifier.fillMaxSize()) {
        if (productList.isEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.shoppinglist),
                contentDescription = "Login-Page",
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 30.dp, bottom = 50.dp)
                    .align(Alignment.TopCenter)
            )
            Text(
                "Add something to your shopping list",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 30.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        } else {
            Column {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    LazyColumn {
                        items(productList) { product ->
                            ProductItem(
                                product = product,
                                onCheckedChange = { viewModel.toggleProductSelected(it) },
                                onDeleteClicked = { viewModel.deleteProductByName(product.itemName) }
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {navController.navigate(HomeRoutes.AddProduct.name)},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Product")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductItem(product: Product, onCheckedChange: (Product) -> Unit, onDeleteClicked: () -> Unit) {
    var isSelected by remember { mutableStateOf(product.isSelected) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(if (isSelected) Color.Green.copy(alpha = 0.2f) else Color.Transparent)
            .border(1.dp, if (isSelected) Color.Green else Color.Black, RoundedCornerShape(4.dp))
            .padding(8.dp).semantics {
                                     testTagsAsResourceId = true
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = {
                isSelected = it
                product.isSelected = it
                onCheckedChange(product)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Green,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = product.itemName,
            modifier = Modifier
                .weight(1f)
                .padding(10.dp).testTag("Item"),
            style = MaterialTheme.typography.titleMedium,
            textDecoration = if (isSelected) TextDecoration.LineThrough else TextDecoration.None
        )
        IconButton(onClick = onDeleteClicked, modifier = Modifier.testTag("Item add button")) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete Product")
        }
    }
}