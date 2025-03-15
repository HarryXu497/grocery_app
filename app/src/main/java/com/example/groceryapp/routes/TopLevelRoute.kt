package com.example.groceryapp.routes

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class TopLevelRoute<T : Any> @OptIn(ExperimentalMaterial3Api::class) constructor(
    val name: String,
    val route: T,
    val icon: ImageVector,
    val topAppBar: @Composable (() -> Unit, TopAppBarScrollBehavior) -> Unit,
)