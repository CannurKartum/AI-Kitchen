package com.example.mythesisapp.common

sealed class Resource<out T : Any> {
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val errorMessage: String) : Resource<Nothing>()
}