package com.example.groceryapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.groceryapp.state.items.ItemWithIdModel

@Composable
fun ItemCard(itemModel: ItemWithIdModel, onClick: (ItemWithIdModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults
            .elevatedCardElevation(),
        onClick = { onClick(itemModel) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                itemModel.itemModel.name,
                style = MaterialTheme.typography.displayLarge
            )

            Box(
                modifier = Modifier
                    .aspectRatio(4.0f / 3.0f)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = itemModel.itemModel.imageURLs[0],
                    contentDescription = itemModel.itemModel.name,
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "\$${itemModel.itemModel.price}",
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    itemModel.itemModel.quantity,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}