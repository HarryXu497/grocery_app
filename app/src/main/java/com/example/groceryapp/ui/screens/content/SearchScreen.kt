package com.example.groceryapp.ui.screens.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceryapp.state.items.ItemWithIdModel
import com.example.groceryapp.state.items.ItemsViewModel
import com.example.groceryapp.ui.composables.ContentScreenAppBar
import com.example.groceryapp.ui.composables.ItemGrid
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenScaffold(
    onItemClick: (ItemWithIdModel) -> Unit,
    onTopBarViewProfile: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchScreenTopBar(
                onViewProfile = onTopBarViewProfile,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SearchScreen(
                onItemClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onItemClick: (ItemWithIdModel) -> Unit,
    itemsViewModel: ItemsViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    val allItems by itemsViewModel.itemList.collectAsStateWithLifecycle()
    var itemList by remember { mutableStateOf(allItems.toList()) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SearchBar(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            query = query,
            onQueryChange = { query = it },
            active = active,
            onActiveChange = { active = it },
            onSearch = {
                active = false
                coroutineScope.launch {
                    if (!query.trim().isEmpty()) {
                        itemList = itemsViewModel.search(query.trim())
                    }
                }
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            },
            trailingIcon = {
                if (active)
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
            },
            placeholder = {
                Text("Search for products", style = MaterialTheme.typography.bodyMedium)
            }
        ) { }

        if (itemList.isEmpty() && !query.trim().isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "No Items Found",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(
                        Alignment.Center
                    ),
                )
            }
        } else {
            ItemGrid(
                itemList =
                    if (query.trim().isEmpty()) {
                        allItems
                    } else {
                        itemList
                    },
                onItemClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenTopBar(onViewProfile: () -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    ContentScreenAppBar(
        title = { Text("Search") },
        onViewProfile = onViewProfile,
        scrollBehavior = scrollBehavior
    )
}