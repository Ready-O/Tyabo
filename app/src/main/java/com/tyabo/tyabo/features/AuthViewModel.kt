package com.tyabo.tyabo.features

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.tyabo.common.UiResult
import com.tyabo.tyabo.AppPresenter
import com.tyabo.data.Token
import com.tyabo.data.UserType
import com.tyabo.repository.interfaces.SessionRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private val _authState = MutableStateFlow<AuthViewState>(AuthViewState.GetStarted)
    val authState : StateFlow<AuthViewState> = _authState.asStateFlow()

    fun updateSessionState(){
        viewModelScope.launch {
            sessionRepository.checkUserToken().onSuccess { token ->
                userRepository.checkUserType(userId = token.id)
                _sessionState.value = SessionState.UserSignedIn(
                    userId = token.id,
                    userType = token.userType
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
                userRepository.getFirebaseUser().onSuccess { user ->
                    if (result.idpResponse!!.isNewUser){
                        _authState.value = AuthViewState.SelectType(user.id, user.name)
                    }
                    else{
                        userRepository.checkUserType(user.id).collectLatest { userTypeResult ->
                            userTypeResult.onSuccess { userType ->
                                authFinish(userId = user.id, userType = userType)
                            }
                                .onFailure {
                                    Timber.e("User Id in Firebase Auth but not in Firestore")
                                }
                        }
                    }
                }.onFailure {
                    appPresenter.displayAuthError()
                }
            }
        }
        else{
            appPresenter.displayAuthError()
        }
    }

    fun onUserTypeSelect(userId: String, name: String, userType: UserType){
        viewModelScope.launch {
            userRepository.addUser(userId = userId, name = name, userType = userType)
            authFinish(userId = userId, userType = userType)
        }
    }

    private suspend fun authFinish(userId: String, userType: UserType){
        sessionRepository.setToken(Token(id = userId, userType = userType))
        _sessionState.value = SessionState.UserSignedIn(userId = userId, userType = userType)
    }

    fun signOut(){
        viewModelScope.launch {
            sessionRepository.signOut()
            _authState.value = AuthViewState.GetStarted
            _sessionState.value = SessionState.UserNotSignedIn
        }
    }

    sealed interface SessionState {
        data class UserSignedIn(val userId: String, val userType: UserType) : SessionState
        object UserNotSignedIn : SessionState
        object Loading : SessionState
    }

    sealed interface AuthViewState  {
        object GetStarted : AuthViewState
        data class SelectType(val id: String, val name: String) : AuthViewState
    }
}
