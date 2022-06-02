package com.tyabo.tyabo.features

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.tyabo.tyabo.AppPresenter
import com.tyabo.data.Token
import com.tyabo.repository.interfaces.SessionRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val appPresenter: AppPresenter
): ViewModel(){

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _authState = MutableStateFlow<AuthViewState>(AuthViewState.GetStarted)
    val authState : StateFlow<AuthViewState> = _authState.asStateFlow()

    fun updateSessionState(){
        viewModelScope.launch {
            sessionRepository.checkUserToken().onSuccess { token ->
                _sessionState.value = SessionState.UserSignedIn(
                    userId = token.id,
                    isChef = token.isChef
                )
            }
                .onFailure {
                    _sessionState.value = SessionState.UserNotSignedIn
                }
        }
    }

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
            viewModelScope.launch {
                userRepository.getFirebaseUser().onSuccess {
                    _authState.value = AuthViewState.SelectType(it.id,it.name)
                }
            }
        }
        else{
            appPresenter.displayAuthError()
        }
    }

    fun onUserTypeSelect(userId: String, isChef: Boolean){
        viewModelScope.launch {
            sessionRepository.setToken(
                Token(id = userId, isChef = isChef)
            )
            _sessionState.value = SessionState.UserSignedIn(userId = userId, isChef = isChef)
        }
    }

    fun signOut(){
        viewModelScope.launch {
            sessionRepository.signOut()
            _sessionState.value = SessionState.UserNotSignedIn
        }
    }
    sealed class SessionState {
        data class UserSignedIn(val userId: String, val isChef: Boolean) : SessionState()
        object UserNotSignedIn : SessionState()
        object Loading : SessionState()
    }

    sealed class AuthViewState  {
        object GetStarted : AuthViewState()
        data class SelectType(val id: String, val name: String) : AuthViewState()
    }
}