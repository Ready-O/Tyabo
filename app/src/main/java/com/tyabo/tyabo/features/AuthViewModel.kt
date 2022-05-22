package com.tyabo.tyabo.features

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.tyabo.tyabo.navigation.GreetingDestination

class AuthViewModel: ViewModel(){

    fun getAuthIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(
                listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.PhoneBuilder().build(),
                )
            )
            .setIsSmartLockEnabled(false)
            .build()
    }

    fun onAuthResult(result: FirebaseAuthUIAuthenticationResult){
        if (result.resultCode == RESULT_OK) {
        } else {

        }
    }


}