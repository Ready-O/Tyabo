package com.tyabo.tyabo.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.tyabo.data.UserType
import com.tyabo.tyabo.auth.AuthViewModel

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
                val name = state.name
                Text(text = "Welcome $name")
                Row() {
                    Button(onClick = { viewModel.onUserTypeSelect(userId = state.id, name = name, userType = UserType.Chef) }){
                        Text(text = "Chef")
                    }
                    Button(onClick = { viewModel.onUserTypeSelect(userId = state.id, name = name, userType = UserType.Restaurant) }){
                        Text(text = "Restaurant")
                    }
                    Button(onClick = { viewModel.onUserTypeSelect(userId = state.id, name = name, userType = UserType.Client) }){
                        Text(text = "Client")
                    }
                }
            }

        }
    }
}