package com.example.groceryapp.routes

import kotlinx.serialization.Serializable

data object Routes {
    @Serializable
    data object Auth {
        @Serializable
        data object SignIn

        @Serializable
        data object Register
    }
    @Serializable
    data object Content {
        @Serializable
        data object Browse

        @Serializable
        data class Item(val id: String)

        @Serializable
        data object Search

        @Serializable
        data object Cart

        @Serializable
        data object Profile
    }
}
