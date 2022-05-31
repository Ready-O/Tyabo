package com.tyabo.tyabo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
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
import com.tyabo.tyabo.navigation.screens.AuthScreen
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

            LaunchedEffect(Unit){
                viewModel.updateSessionState()
            }
            when(sessionState) {
                AuthViewModel.SessionState.UserNotSignedIn -> {
                    AuthScreen(viewModel)
                }
                AuthViewModel.SessionState.Loading -> {
                    CircularProgressIndicator()
                }
                is AuthViewModel.SessionState.UserSignedIn -> {
                    val state = sessionState as AuthViewModel.SessionState.UserSignedIn
                    Column() {
                        Text(text = "id ${state.userId} - chef ${state.isChef}")
                        MainAppLayout(appPresenter = appPresenter)
                    }
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
