package com.example.groceryapp.ui.screens.content

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.groceryapp.state.auth.AuthViewModel
import com.example.groceryapp.state.cart.CartItemsViewModel
import com.example.groceryapp.ui.composables.ContentScreenAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.groceryapp.state.items.ItemWithIdModel
import com.example.groceryapp.ui.composables.CartItemList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenScaffold(
    onTopBarViewProfile: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CartScreenTopBar(
                onViewProfile = onTopBarViewProfile,
                scrollBehavior = scrollBehavior,
            )
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            CartScreen()
        }
    }
}

@Composable
fun CartScreen(
    authViewModel: AuthViewModel = viewModel(),
    cartItemsViewModel: CartItemsViewModel = viewModel()
) {
    val currentUser by authViewModel.authState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    if (currentUser == null) {
        LoadingScreen()
    } else {
        cartItemsViewModel.startSnapshotListener(currentUser!!)
        val itemList by cartItemsViewModel.itemList.collectAsStateWithLifecycle()

        if (itemList == null) {
            LoadingScreen()
        } else if (itemList!!.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    "No Items in your Cart",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(alignment = Alignment.Center),
                )
            }
        } else {
            CartItemList(
                cartItemList = itemList!!,
                onCartItemCountChange = { cartItem, newCount ->
                    coroutineScope.launch {
                        cartItemsViewModel.modifyCartItemCount(
                            currentUser!!.uid,
                            cartItem.itemWithIdModel.id,
                            newCount,
                        )
                    }
                },
                onCartItemDelete = { cartItem ->
                    coroutineScope.launch {
                        cartItemsViewModel.deleteCartItem(
                            currentUser!!.uid,
                            cartItem.itemWithIdModel.id,
                        )
                    }
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenTopBar(onViewProfile: () -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    ContentScreenAppBar(
        title = { Text("Cart") },
        onViewProfile = onViewProfile,
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .align(alignment = Alignment.Center),
        )
    }
}