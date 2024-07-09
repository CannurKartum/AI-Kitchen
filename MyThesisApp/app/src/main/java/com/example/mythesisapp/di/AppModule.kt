package com.example.mythesisapp.di

import com.example.mythesisapp.data.model.FoodRepository
import com.example.mythesisapp.data.repository.FirestoreFoodRepository
import com.example.mythesisapp.ui.mealplanner.MealPlannerViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {
    @Provides
    fun provideFoodRepository(): FoodRepository = FirestoreFoodRepository()

    @ViewModelScoped
    @Provides
    fun provideFoodViewModel(repository: FoodRepository): MealPlannerViewModel =
        MealPlannerViewModel(repository)
}