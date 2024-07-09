package com.example.mythesisapp.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mythesisapp.common.Resource
import com.example.mythesisapp.data.model.Product
import com.example.mythesisapp.data.model.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class ProductRepository(
    private val firestore: FirebaseFirestore,
) {
    private val productRef = firestore.collection("shoppinglistitem")
    private val recipesCollection = firestore.collection("recipes")

    suspend fun addProduct(name: String): Resource<Unit> {
        val product = Product(name)
        var result: Resource<Unit> = Resource.Error("An unknown error occurred")
        withContext(Dispatchers.IO) {
            try {
                productRef.add(product)
                    .addOnCompleteListener {
                        result = if (it.isSuccessful) {
                            Resource.Success(Unit)
                        } else {
                            Resource.Error(it.exception?.message.orEmpty())
                        }
                    }.await()
            } catch (e: Exception) {
                result = Resource.Error(e.message.orEmpty())
            }
        }
        return result
    }


    fun observeProducts(): LiveData<Resource<List<Product>>> {
        val liveData = MutableLiveData<Resource<List<Product>>>()

        productRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                liveData.postValue(Resource.Error(e.message ?: "Error fetching data"))
                return@addSnapshotListener
            }

            val products = snapshot?.documents?.mapNotNull {
                it.toObject(Product::class.java)
            }
            liveData.postValue(Resource.Success(products ?: listOf()))
        }

        return liveData
    }

    suspend fun deleteProductByName(productName: String): Resource<Unit> = withContext(Dispatchers.IO) {
        try {

            val querySnapshot = productRef.whereEqualTo("itemName", productName).get().await()


            if (querySnapshot.isEmpty) {
                Log.d("DeleteProduct", "No products found with the name: $productName")
                return@withContext Resource.Error("No product found with name: $productName")
            }


            querySnapshot.documents.forEach { document ->
                document.reference.delete().await()
            }

            Log.d("DeleteProduct", "Product deleted successfully")
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("DeleteProduct", "Failed to delete product: ${e.message}", e)
            Resource.Error(e.message ?: "Failed to delete product")
        }
    }


    suspend fun addRecipe(recipe: Recipe): Resource<Unit> {
        return try {
            recipesCollection.add(recipe).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    fun getRecipes(): Flow<Resource<List<Recipe>>> = callbackFlow {
        val subscription = recipesCollection
            .addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null && e == null) {
                    val recipes = snapshot.documents.map { document ->
                        document.toObject(Recipe::class.java)?.copy(id = document.id) ?: Recipe()
                    }
                    Resource.Success(recipes)
                } else {
                    Resource.Error(e?.message ?: "Unknown error fetching recipes")
                }
                trySend(response).isSuccess
            }
        awaitClose { subscription.remove() }
    }

    suspend fun getRecipeDetail(id: String): Recipe? {
        val result = recipesCollection.document(id).get().await()
        return result.toObject(Recipe::class.java)?.copy(id = result.id)
    }

    suspend fun updateRecipe(updatedRecipe: Recipe): Resource<Unit> {
        return try {
            recipesCollection.document(updatedRecipe.id).set(updatedRecipe).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update recipe")
        }
    }

    suspend fun deleteRecipe(recipeId: String): Resource<Unit> {
        return try {
            recipesCollection.document(recipeId).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete recipe")
        }
    }
}




