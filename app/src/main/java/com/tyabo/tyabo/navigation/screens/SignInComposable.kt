package com.tyabo.tyabo.navigation.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.tyabo.tyabo.features.AuthViewModel
import com.tyabo.tyabo.navigation.SignInDestination
import timber.log.Timber

fun NavGraphBuilder.signInComposable(navigateToHome: () -> Unit ) = composable(
    route = SignInDestination.route
){
    val viewModel: AuthViewModel = hiltViewModel()

    val sessionState by viewModel.sessionState.collectAsState()

    val launchAuth = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract()
    ) { result ->
        viewModel.onAuthResult(result, navigateToHome)
    }

    LaunchedEffect(Unit){
        viewModel.updateSessionSate()
    }

    when(sessionState) {
        AuthViewModel.SessionState.UserNotSignedIn -> {
            Button(onClick = {launchAuth.launch(viewModel.getAuthIntent())}){
                Text(text = "login")
            }
        }
        AuthViewModel.SessionState.Loading -> {
            CircularProgressIndicator()
        }
    }


}

