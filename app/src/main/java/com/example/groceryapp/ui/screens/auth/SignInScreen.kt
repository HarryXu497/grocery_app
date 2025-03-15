package com.example.groceryapp.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.groceryapp.ui.composables.AuthForm

@Composable
fun SignInScreenScaffold(
    onSubmit: (String, String) -> Unit,
    onAlternate: () -> Unit,
) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            SignInScreen(
                onSubmit = onSubmit,
                onAlternate = onAlternate
            )
        }
    }
}

@Composable
fun SignInScreen(
    onSubmit: (String, String) -> Unit,
    onAlternate: () -> Unit
) {
    var emailField by remember { mutableStateOf("") }
    var passwordField by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AuthForm(
            titleString = "Sign In",
            submitString = "Sign In",
            alternateString = "Register Instead",
            onSubmit = { onSubmit(emailField, passwordField) },
            onAlternate = onAlternate
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = emailField,
                onValueChange = { emailField = it },
                label = { Text("Email") },
                singleLine = true,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordField,
                onValueChange = { passwordField = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation =
                    if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                trailingIcon = {
                    Icon(
                        if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (showPassword) "Show Password" else "Hide Password",
                        modifier = Modifier
                            .requiredSize(24.dp)
                            .clickable { showPassword = !showPassword }
                    )
                },
            )
        }
    }
}
