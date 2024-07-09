package com.example.mythesisapp.data.repository

import com.example.mythesisapp.data.model.Food
import com.example.mythesisapp.data.model.FoodRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreFoodRepository : FoodRepository {
    private val db = FirebaseFirestore.getInstance()

    override fun fetchFoods(collectionName: String): Flow<List<Food>> = callbackFlow {
        val collection = db.collection(collectionName)
        val listenerRegistration = collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                trySend(emptyList<Food>())
                close(e)
            } else {
                val foods = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject<Food>()
                } ?: emptyList()
                trySend(foods)
            }
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    override suspend fun addFood(collectionName: String, food: Food) {
        val collection = db.collection(collectionName)
        collection.add(food).await()
    }

    override suspend fun deleteFood(collectionName: String, foodName: String) {
        val collection = db.collection(collectionName)
        collection.whereEqualTo("foodName", foodName).get().await().forEach { document ->
            document.reference.delete().await()
        }
    }
}