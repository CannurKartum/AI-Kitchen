package com.example.mythesisapp.ui.savedrecipes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.common.Resource
import com.example.mythesisapp.data.model.Recipe
import com.example.mythesisapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    var searchText = mutableStateOf("")

    private val _uiState = MutableStateFlow(SavedRecipesUIState())
    val uiState: StateFlow<SavedRecipesUIState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            repository.getRecipes().collect { resource ->
                when (resource) {
                    is Resource.Success -> setState {
                        copy(
                            isLoading = false,
                            recipes = resource.data,
                            filteredRecipes = resource.data
                        )
                    }

                    is Resource.Error -> setState { copy(isLoading = false, errorMessage = resource.errorMessage) }
                }
            }
        }
    }

    fun addRecipe(recipe: Recipe) = viewModelScope.launch {
        setState { copy(isLoading = true) }

        when (val result = repository.addRecipe(recipe)) {
            is Resource.Success -> setState {
                copy(
                    isSuccessRecipeAdded = true,
                    isLoading = false,
                    errorMessage = null
                )
            }

            is Resource.Error -> setState { copy(errorMessage = result.errorMessage, isLoading = false) }
        }
        loadRecipes()
    }

    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch {
        if (recipe.id.isNotEmpty()) {
            setState { copy(isLoading = true) }
            when (val result = repository.deleteRecipe(recipe.id)) {
                is Resource.Success -> {
                    // Successfully deleted the recipe
                    setState { copy(isSuccessRecipeDeleted = true, isLoading = false, errorMessage = null) }
                }

                is Resource.Error -> {
                    setState { copy(errorMessage = result.errorMessage, isLoading = false) }
                }
            }
        } else {
            setState { copy(errorMessage = "Recipe ID is empty", isLoading = false) }
        }
    }

    fun onSearchQueryChanged(query: String) = viewModelScope.launch {
        searchText.value = query
        setState { copy(isLoading = true) }

        if (query.isEmpty()) {
            setState { copy(isLoading = false, filteredRecipes = recipes) }
            return@launch
        } else {
            val filteredRecipes = _uiState.value.recipes.filter {
                it.title.contains(query, ignoreCase = true)
            }
            setState { copy(isLoading = false, filteredRecipes = filteredRecipes) }
        }
    }

    private fun setState(reducer: SavedRecipesUIState.() -> SavedRecipesUIState) {
        _uiState.update {
            reducer(it)
        }
    }
}

data class SavedRecipesUIState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val filteredRecipes: List<Recipe> = emptyList(),
    val isSuccessRecipeAdded: Boolean = false,
    val isSuccessRecipeDeleted: Boolean = false,
    val isSuccessRecipeUpdated: Boolean = false,
    val errorMessage: String? = null
)