package com.tyabo.tyabo.navigation.screens

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.tyabo.tyabo.features.AuthViewModel
import com.tyabo.tyabo.navigation.GreetingDestination
import com.tyabo.tyabo.navigation.SignInDestination

fun NavGraphBuilder.signInComposable(navigateToHome: () -> Unit ) = composable(
    route = SignInDestination.route
){
    val viewModel: AuthViewModel = hiltViewModel()

    val launchAuth = rememberLauncherForActivityResult(
        contract = FirebaseAuthUIActivityResultContract()
    ) { result ->
        viewModel.onAuthResult(result)
    }

    LaunchedEffect(Unit) {
        launchAuth.launch(viewModel.getAuthIntent())
    }
}

