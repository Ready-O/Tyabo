package com.tyabo.tyabo.navigation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.tyabo.tyabo.features.AuthViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel) {

    val authState by viewModel.authState.collectAsState()

    when(authState){
        AuthViewModel.AuthViewState.GetStarted -> {

            val launchAuth = rememberLauncherForActivityResult(
                contract = FirebaseAuthUIActivityResultContract()
            ) { result ->
                viewModel.onAuthResult(result)
            }

            Button(onClick = {launchAuth.launch(viewModel.getAuthIntent())}){
                Text(text = "login")
            }

        }

        is AuthViewModel.AuthViewState.SelectType -> {
            val state = authState as AuthViewModel.AuthViewState.SelectType
            Column() {
                Text(text = "Welcome ${state.name}")
                Row() {
                    Button(onClick = { viewModel.onUserTypeSelect(userId = state.id, isChef = true) }){
                        Text(text = "Chef")
                    }
                    Button(onClick = { viewModel.onUserTypeSelect(userId = state.id, isChef = false) }){
                        Text(text = "Client")
                    }
                }
            }

        }
    }
}