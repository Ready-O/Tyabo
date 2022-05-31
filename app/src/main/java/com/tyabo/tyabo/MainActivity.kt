package com.tyabo.tyabo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.tyabo.tyabo.features.AuthViewModel
import com.tyabo.tyabo.navigation.GreetingDestination
import com.tyabo.tyabo.navigation.screens.greetingsComposable
import com.tyabo.tyabo.ui.theme.TyaboTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appPresenter: AppPresenter
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sessionState by viewModel.sessionState.collectAsState()

            val launchAuth = rememberLauncherForActivityResult(
                contract = FirebaseAuthUIActivityResultContract()
            ) { result ->
                viewModel.onAuthResult(result)
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
                AuthViewModel.SessionState.UserSignedIn -> {
                    MainAppLayout(appPresenter = appPresenter)
                }
            }
        }
    }
}

@Composable
fun MainAppLayout(appPresenter: AppPresenter) {

    val bannerViewState by appPresenter.bannerViewState.collectAsState()

    TyaboTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = GreetingDestination.route
            ) {
                greetingsComposable()
            }
            BannerLayout(bannerViewState)
        }
    }
}

@Composable
fun BannerLayout(bannerViewState: BannerViewState?) {
    when (bannerViewState) {
        is BannerViewState.Default -> {
            Text(text = bannerViewState.message)
        }
        is BannerViewState.Error -> {
            Text(text = bannerViewState.message)
        }
        null -> {}
    }
}
