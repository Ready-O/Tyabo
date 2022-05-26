package com.tyabo.tyabo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tyabo.tyabo.navigation.GreetingDestination
import com.tyabo.tyabo.navigation.SignInDestination
import com.tyabo.tyabo.navigation.screens.greetingsComposable
import com.tyabo.tyabo.navigation.screens.signInComposable
import com.tyabo.tyabo.ui.theme.TyaboTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appPresenter: AppPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppLayout(appPresenter = appPresenter)
        }
    }
}

@Composable
fun AppLayout(appPresenter: AppPresenter) {

    val bannerViewState by appPresenter.bannerViewState.collectAsState()

    TyaboTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SignInDestination.route
            ) {
                signInComposable(navigateToHome = {navController.navigate(GreetingDestination.route)})
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
