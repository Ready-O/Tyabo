package com.tyabo.tyabo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.tyabo.data.UserType
import com.tyabo.tyabo.auth.AuthViewModel
import com.tyabo.tyabo.navigation.chef.ChefNavHost
import com.tyabo.tyabo.auth.AuthScreen
import com.tyabo.tyabo.ui.AppPresenter
import com.tyabo.tyabo.ui.BannerViewState
import com.tyabo.designsystem.theme.TyaboTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appPresenter: AppPresenter
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TyaboTheme {
                val sessionState by viewModel.sessionState.collectAsState()
                val bannerViewState by appPresenter.bannerViewState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.updateSessionState()
                }
                when (sessionState) {
                    AuthViewModel.SessionState.UserNotSignedIn -> {
                        Surface() {
                            AuthScreen(viewModel)
                            BannerLayout(bannerViewState)
                        }
                    }
                    AuthViewModel.SessionState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is AuthViewModel.SessionState.UserSignedIn -> {
                        val state = sessionState as AuthViewModel.SessionState.UserSignedIn
                        Column() {
                            /*
                            Text(text = "id ${state.userId} - type ${state.userType}")
                            Button(onClick = viewModel::signOut) {
                                Text(text = "Logout")
                            }

                             */
                            MainAppLayout(
                                bannerViewState = bannerViewState,
                                userType = state.userType
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppLayout(userType: UserType, bannerViewState: BannerViewState?) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        when(userType){
            UserType.Chef -> ChefNavHost(navController = navController)
        }
        BannerLayout(bannerViewState)
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
