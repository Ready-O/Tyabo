package com.tyabo.tyabo.navigation.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tyabo.tyabo.navigation.SignInDestination
import com.tyabo.tyabo.navigation.scopedNav.scopedComposable
import com.tyabo.tyabo.ui.theme.TyaboTheme

fun NavGraphBuilder.signInComposable() = scopedComposable<SignInDestination>(
    route = SignInDestination.route
){ _,_ ->
    Greeting(name = "Android")
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TyaboTheme {
        Greeting("Android")
    }
}