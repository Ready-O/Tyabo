package com.tyabo.tyabo.navigation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.tyabo.tyabo.features.AuthViewModel
import com.tyabo.tyabo.navigation.SignInDestination
import com.tyabo.tyabo.navigation.scopedNav.scopedComposable
import com.tyabo.tyabo.ui.theme.TyaboTheme

fun NavGraphBuilder.signInComposable() = scopedComposable<SignInDestination>(
    route = SignInDestination.route
){ _,scope ->
    val viewModel: AuthViewModel = scope.get()
}

