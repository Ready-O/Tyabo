package com.tyabo.tyabo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppLayout()
        }
    }
}

@Composable
fun AppLayout() {
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
        }
    }
}
