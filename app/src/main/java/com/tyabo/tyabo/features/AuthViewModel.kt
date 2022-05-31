package com.tyabo.tyabo.features

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.tyabo.tyabo.AppPresenter
import com.tyabo.tyabo.navigation.GreetingDestination
import com.tyabo.tyabo.repository.SessionRepository
import com.tyabo.tyabo.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val appPresenter: AppPresenter
): ViewModel(){

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

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

    fun onAuthResult(result: FirebaseAuthUIAuthenticationResult) {
            if (result.resultCode == RESULT_OK) {
                _sessionState.value = SessionState.UserSignedIn
                viewModelScope.launch {
                    userRepository.signIn().onSuccess {
                        sessionRepository.setToken(it)
                    }
                }
            }
            else{
                appPresenter.displayAuthError()
            }
    }

    fun updateSessionSate(){
        viewModelScope.launch {
           if (sessionRepository.checkUserToken()){
               _sessionState.value = SessionState.UserSignedIn
           }
            else{
                _sessionState.value = SessionState.UserNotSignedIn
           }
        }
    }

    sealed class SessionState {
        object UserSignedIn : SessionState()
        object UserNotSignedIn : SessionState()
        object Loading : SessionState()
    }
}