package com.example.groceryapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun AuthForm(
    titleString: String,
    submitString: String,
    alternateString: String,
    onSubmit: () -> Unit,
    onAlternate: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.7f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            titleString,
            style = MaterialTheme.typography.displayLarge
        )
        content()
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.SpaceBetween) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = onSubmit) {
                Text(submitString, style = MaterialTheme.typography.bodyMedium)
            }
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = onAlternate) {
                Text(alternateString, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}