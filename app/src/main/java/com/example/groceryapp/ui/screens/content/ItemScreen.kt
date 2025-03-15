package com.example.groceryapp.ui.screens.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.groceryapp.state.auth.AuthViewModel
import com.example.groceryapp.state.cart.CartItemsViewModel
import com.example.groceryapp.state.items.ItemsViewModel
import com.example.groceryapp.ui.composables.StyledTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreenScaffold(
    itemId: String,
    onNavigateBack: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ItemScreenAppBar(
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = bottomBar,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ItemScreen(
                itemId = itemId
            )
        }
    }
}

@Composable
fun ItemScreen(
    itemId: String,
    authViewModel: AuthViewModel = viewModel(),
    cartItemsViewModel: CartItemsViewModel = viewModel(),
    itemsViewModel: ItemsViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val currentUser by authViewModel.authState.collectAsStateWithLifecycle()
    val item by itemsViewModel.getItemWithIdFlow(itemId)
        .collectAsStateWithLifecycle(null)

    if (item == null || currentUser == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        val pagerState = rememberPagerState(pageCount = { item!!.itemModel.imageURLs.size })

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CardDefaults.cardColors().containerColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .background(CardDefaults.cardColors().containerColor),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(item!!.itemModel.name, style = MaterialTheme.typography.displayLarge)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4.0f / 3.0f)
                        .background(Color.White),
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) { pageIndex ->
                        val image = item!!.itemModel.imageURLs[pageIndex]

                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(shape = RoundedCornerShape(8.dp)),
                            model = image,
                            contentDescription = item!!.itemModel.name,
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(16.dp)
                            )
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "\$${item!!.itemModel.price} for ${item!!.itemModel.quantity}",
                        style = MaterialTheme.typography.displayMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                cartItemsViewModel.addCartItem(currentUser!!.uid, item!!)
                            }
                        }
                    ) {
                        Text("Add to Cart", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreenAppBar(onNavigateBack: () -> Unit) {
    StyledTopAppBar(
        title = { Text("View Item") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
    )
}