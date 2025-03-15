package com.example.groceryapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.groceryapp.state.items.ItemWithIdModel

@Composable
fun ItemGrid(itemList: List<ItemWithIdModel>, onItemClick: (ItemWithIdModel) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.padding(12.dp),
        columns = GridCells.Adaptive(256.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            itemList,
            key = { it.id }
        ) { item ->
            ItemCard(item, onClick = onItemClick)
        }
    }
}