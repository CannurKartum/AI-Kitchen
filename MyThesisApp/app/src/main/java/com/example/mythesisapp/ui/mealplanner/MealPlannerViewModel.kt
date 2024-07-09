package com.example.mythesisapp.ui.mealplanner


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythesisapp.data.model.Food
import com.example.mythesisapp.data.model.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _breakfastFoods = MutableStateFlow<List<Food>>(emptyList())
    val breakfastFoods: StateFlow<List<Food>> = _breakfastFoods.asStateFlow()

    private val _lunchFoods = MutableStateFlow<List<Food>>(emptyList())
    val lunchFoods: StateFlow<List<Food>> = _lunchFoods.asStateFlow()

    private val _dinnerFoods = MutableStateFlow<List<Food>>(emptyList())
    val dinnerFoods: StateFlow<List<Food>> = _dinnerFoods.asStateFlow()

    init {
        fetchFoods("breakfastfood")
        fetchFoods("lunchfood")
        fetchFoods("dinnerfood")
    }

    private fun fetchFoods(collectionName: String) {
        viewModelScope.launch {
            repository.fetchFoods(collectionName).collect { foods ->
                when (collectionName) {
                    "breakfastfood" -> _breakfastFoods.value = foods
                    "lunchfood" -> _lunchFoods.value = foods
                    "dinnerfood" -> _dinnerFoods.value = foods
                }
            }
        }
    }

    fun addFood(collectionName: String, foodName: String) {
        viewModelScope.launch {
            repository.addFood(collectionName, Food(foodName = foodName))
        }
    }

    fun deleteFood(collectionName: String, foodName: String) {
        viewModelScope.launch {
            repository.deleteFood(collectionName, foodName)
        }
    }

}
