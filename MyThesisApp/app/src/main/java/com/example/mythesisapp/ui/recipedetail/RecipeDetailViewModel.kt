package com.example.mythesisapp.ui.recipedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.common.Resource
import com.example.mythesisapp.data.model.Recipe
import com.example.mythesisapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUIState())
    val uiState: StateFlow<RecipeDetailUIState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("id")?.let { id ->
            getRecipe(id)
        }
    }

    private fun getRecipe(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getRecipeDetail(id)?.let {
                _uiState.value = _uiState.value.copy(
                    recipe = it,
                    id = it.id,
                    title = it.title,
                    ingredient = it.ingredient,
                    description = it.description,
                    cookTime = it.cooktime,
                    isLoading = false
                )
            } ?: run {
                _uiState.value = _uiState.value.copy(errorMessage = "Recipe not found", isLoading = false)
            }
        }
    }

    fun updateRecipe() = viewModelScope.launch {
        if (_uiState.value.title.isEmpty() || _uiState.value.ingredient.isEmpty() || _uiState.value.description.isEmpty() || _uiState.value.cookTime.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please fill out all fields")
            return@launch
        }

        _uiState.value = _uiState.value.copy(isLoading = true)
        val result = repository.updateRecipe(
            Recipe(
                id = _uiState.value.id,
                title = _uiState.value.title,
                ingredient = _uiState.value.ingredient,
                description = _uiState.value.description,
                cooktime = _uiState.value.cookTime
            )
        )
        _uiState.value = when (result) {
            is Resource.Success -> _uiState.value.copy(isSuccessRecipeUpdated = true, isLoading = false)
            is Resource.Error -> _uiState.value.copy(errorMessage = result.errorMessage, isLoading = false)
        }
    }

    fun deleteRecipe(recipe: Recipe) = viewModelScope.launch {
        if (recipe.id.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repository.deleteRecipe(recipe.id)
            _uiState.value = when (result) {
                is Resource.Success -> {

                    _uiState.value.copy(isSuccessRecipeDeleted = true, isLoading = false, errorMessage = null)
                }

                is Resource.Error -> {

                    _uiState.value.copy(errorMessage = result.errorMessage, isLoading = false)
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(errorMessage = "Recipe ID is empty, cannot delete", isLoading = false)
        }
    }

    fun onTitleChange(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun onIngredientChange(ingredient: String) {
        _uiState.value = _uiState.value.copy(ingredient = ingredient)
    }

    fun onDescriptionChange(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun onCookTimeChange(cookTime: String) {
        _uiState.value = _uiState.value.copy(cookTime = cookTime)
    }
}

data class RecipeDetailUIState(
    val isLoading: Boolean = false,
    val recipe: Recipe? = null,
    val id: String = "",
    val title: String = "",
    val ingredient: String = "",
    val description: String = "",
    val cookTime: String = "",
    val isSuccessRecipeDeleted: Boolean = false,
    val isSuccessRecipeUpdated: Boolean = false,
    val errorMessage: String? = null
)