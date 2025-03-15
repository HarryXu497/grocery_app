package com.example.groceryapp.ui.screens.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceryapp.state.auth.AuthViewModel
import com.example.groceryapp.ui.composables.StyledTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenScaffold(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ProfileScreenAppBar(
                onNavigateBack = onNavigateBack,
                onSignOut = onSignOut
            )
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.authState.collectAsStateWithLifecycle()

    if (currentUser == null) {
        LoadingScreen()
    } else {
        Text(currentUser!!.displayName.toString(), style = MaterialTheme.typography.displayLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenAppBar(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    StyledTopAppBar(
        title = { Text("Profile") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                authViewModel.signOut()
                onSignOut()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Sign Out"
                )
            }
        }
    )
}