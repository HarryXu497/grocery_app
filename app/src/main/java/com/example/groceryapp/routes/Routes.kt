package com.example.groceryapp.routes

import kotlinx.serialization.Serializable

sealed class Route {
    fun toRouteString(): String {
        return this::class.qualifiedName.toString()
    }
}

data object Routes: Route() {
    @Serializable
    data object Auth: Route() {
        @Serializable
        data object SignIn: Route()

        @Serializable
        data object Register: Route()
    }
    @Serializable
    data object Content: Route() {
        @Serializable
        data object Browse: Route()

        @Serializable
        data class Item(val id: String): Route()

        @Serializable
        data object Search: Route()

        @Serializable
        data object Cart: Route()

        @Serializable
        data object Profile: Route()
    }
}
