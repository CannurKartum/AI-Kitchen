package com.example.mythesisapp.data.model

import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    fun fetchFoods(collectionName: String): Flow<List<Food>>
    suspend fun addFood(collectionName: String, food: Food)
    suspend fun deleteFood(collectionName: String, foodId: String)
}