package com.example.groceryapp.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreenAppBar(
    title: @Composable () -> Unit,
    onViewProfile: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    StyledTopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = onViewProfile) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Open Profile Page"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}