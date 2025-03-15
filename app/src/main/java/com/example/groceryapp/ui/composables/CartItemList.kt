package com.example.groceryapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.groceryapp.state.cart.CartItemWithIdModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartItemList(
    cartItemList: List<CartItemWithIdModel>,
    onCartItemDelete: (CartItemWithIdModel) -> Unit,
    onCartItemCountChange: (CartItemWithIdModel, Long) -> Unit,
) {
    LazyColumn {
        items(cartItemList, key = { it.itemWithIdModel.id }) { cartItem ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .animateItem(fadeInSpec = null, fadeOutSpec = null)
            ) {
                SwipeToDismissWrapper(
                    cartItem = cartItem,
                    onDelete = { onCartItemDelete(it) },
                ) {
                    CartItemListTile(
                        cartItemWithIdModel = cartItem,
                        onCartItemCountChange = onCartItemCountChange
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissWrapper(
    cartItem: CartItemWithIdModel,
    onDelete: (CartItemWithIdModel) -> Unit,
    content: @Composable (CartItemWithIdModel) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { newValue ->
            if (newValue == SwipeToDismissBoxValue.EndToStart) {
                onDelete(cartItem)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromEndToStart = true,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.hsl(0.0f, 0.75f, 0.55f)),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Delete,
                        modifier = Modifier.padding(end = 18.dp),
                        tint = Color.White,
                        contentDescription = "delete"
                    )
                }
            }
        }
    ) {
        content(cartItem)
    }
}