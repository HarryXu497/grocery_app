package com.example.groceryapp.ui.screens.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceryapp.state.items.ItemWithIdModel
import com.example.groceryapp.state.items.ItemsViewModel
import com.example.groceryapp.ui.composables.ContentScreenAppBar
import com.example.groceryapp.ui.composables.ItemGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreenScaffold(
    onTopBarViewProfile: () -> Unit,
    onItemClick: (ItemWithIdModel) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BrowseScreenTopBar(
                onViewProfile = onTopBarViewProfile,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            BrowseScreen(
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun BrowseScreen(
    onItemClick: (ItemWithIdModel) -> Unit,
    itemsViewModel: ItemsViewModel = viewModel()
) {
    val itemList by itemsViewModel.itemList.collectAsStateWithLifecycle()

    ItemGrid(itemList, onItemClick = onItemClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreenTopBar(onViewProfile: () -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    ContentScreenAppBar(
        title = { Text("Browse") },
        onViewProfile = onViewProfile,
        scrollBehavior = scrollBehavior,
    )
}