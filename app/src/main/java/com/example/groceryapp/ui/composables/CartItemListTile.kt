package com.example.groceryapp.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.groceryapp.state.cart.CartItemWithIdModel

@Composable
fun CartItemListTile(
    cartItemWithIdModel: CartItemWithIdModel,
    onCartItemCountChange: (CartItemWithIdModel, Long) -> Unit
) {
    val (
        _,
        imageURLs,
        name,
        _,
        price,
        quantity,
    ) = cartItemWithIdModel.itemWithIdModel.itemModel

    val quantitySelected = cartItemWithIdModel.quantitySelected

    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f, fill = false)
            ) {
                Box(
                    modifier = Modifier
                        .aspectRatio(4.0f / 3.0f)
                        .padding(4.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .background(Color.White),
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = imageURLs[0],
                        contentDescription = name,
                        contentScale = ContentScale.Crop
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text("$price for $quantity", style = MaterialTheme.typography.bodyMedium)

                }
            }

            Counter(
                count = quantitySelected,
                onCountChange = { onCartItemCountChange(cartItemWithIdModel, it) }
            )
        }
    }
}

@Composable
fun Counter(
    count: Long,
    onCountChange: (Long) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = { onCountChange(count - 1) }
        ) {
            Icon(Icons.Filled.Remove, contentDescription = "Remove")
        }
        Text(
            count.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
        IconButton(
            onClick = { onCountChange(count + 1) }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Remove")
        }
    }
}