package com.example.mythesisapp.di

import com.example.mythesisapp.data.model.FoodRepository
import com.example.mythesisapp.data.repository.AuthRepository
import com.example.mythesisapp.data.repository.FirestoreFoodRepository
import com.example.mythesisapp.data.repository.ProductRepository
import com.example.mythesisapp.data.repository.StorageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepository(firebaseAuth)
    }

    @Provides
    fun provideListRepository(firestore: FirebaseFirestore): ProductRepository {
        return ProductRepository(firestore)
    }

}

