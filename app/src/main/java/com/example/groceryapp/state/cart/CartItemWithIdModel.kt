package com.example.groceryapp.state.cart

import com.example.groceryapp.state.items.ItemWithIdModel
import kotlin.String

data class CartItemWithIdModel(
    val quantitySelected: Long,
    val itemWithIdModel: ItemWithIdModel
) {
    fun toFlatFirebaseObject(): Map<String, Any> {
        return mapOf(
            "quantitySelected" to quantitySelected,
            "category" to itemWithIdModel.itemModel.category,
            "imageURLs" to itemWithIdModel.itemModel.imageURLs,
            "name" to itemWithIdModel.itemModel.name,
            "nameLowercase" to itemWithIdModel.itemModel.nameLowercase,
            "price" to itemWithIdModel.itemModel.price,
            "quantity" to itemWithIdModel.itemModel.quantity,
        )
    }
}
