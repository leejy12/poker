package com.example.poker.Data

data class ResponseType<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)
