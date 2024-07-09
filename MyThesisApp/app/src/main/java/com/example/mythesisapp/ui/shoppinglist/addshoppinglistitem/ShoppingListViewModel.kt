package com.example.mythesisapp.ui.shoppinglist.addshoppinglistitem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.common.Resource
import com.example.mythesisapp.data.model.Product
import com.example.mythesisapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {


    var shoppingListUIState by mutableStateOf(ShoppingListUIState())
        private set

    init {
        initiateProductObserver()
    }

    fun itemNameChanged(itemName: String) {
        shoppingListUIState = shoppingListUIState.copy(itemName = itemName)
    }

    private fun initiateProductObserver() {
        repository.observeProducts().observeForever { resource ->
            when (resource) {
                is Resource.Success -> {
                    shoppingListUIState = shoppingListUIState.copy(
                        productList = resource.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    shoppingListUIState = shoppingListUIState.copy(
                        isLoading = false,
                        errorMessage = resource.errorMessage
                    )
                }
            }
        }
    }

    fun addProduct() = viewModelScope.launch {
        shoppingListUIState = shoppingListUIState.copy(isLoading = true)
        when (val result = repository.addProduct(shoppingListUIState.itemName)) {
            is Resource.Success -> {
                shoppingListUIState = shoppingListUIState.copy(
                    isLoading = false,
                    isSuccessItemAdded = true
                )
            }
            is Resource.Error -> {
                shoppingListUIState = shoppingListUIState.copy(
                    isLoading = false,
                    isSuccessItemAdded = false,
                    errorMessage = result.errorMessage
                )
            }
        }
    }

    fun deleteProductByName(productName: String) = viewModelScope.launch {
        shoppingListUIState = shoppingListUIState.copy(isLoading = true)
        when (val result = repository.deleteProductByName(productName)) {
            is Resource.Success -> {
                shoppingListUIState = shoppingListUIState.copy(
                    isLoading = false,
                    isSuccessItemDeleted = true
                )
            }
            is Resource.Error -> {
                shoppingListUIState = shoppingListUIState.copy(
                    isLoading = false,
                    isSuccessItemDeleted = false,
                    errorMessage = result.errorMessage
                )
            }
        }
    }

    fun toggleProductSelected(product: Product) {
        val updatedList = shoppingListUIState.productList.map {
            if (it.itemName == product.itemName) it.copy(isSelected = !it.isSelected) else it
        }
        shoppingListUIState = shoppingListUIState.copy(productList = updatedList)
    }


}

data class ShoppingListUIState(
    val itemName: String = "",
    val productList: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isSuccessItemAdded: Boolean = false,
    val isSuccessItemDeleted: Boolean = false,
    val errorMessage: String? = null
)