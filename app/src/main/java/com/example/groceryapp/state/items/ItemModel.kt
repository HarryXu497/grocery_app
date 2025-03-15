package com.example.groceryapp.state.items;

data class ItemModel(
    val category: String,
    val imageURLs: List<String>,
    val name: String,
    val nameLowercase: String,
    val price: Double,
    val quantity: String,
)
