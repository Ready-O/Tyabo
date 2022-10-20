package com.tyabo.feature.chef.profile.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.repository.interfaces.ChefProfileRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chefRepository: ChefProfileRepository
) : ViewModel(){

    private val userId = userRepository.getUserId()

    fun fetchProfileFirstTime(){
        viewModelScope.launch {
            chefRepository.updateStateChef(userId)
        }
    }

    val chefState: StateFlow<ChefViewState> = chefRepository.chefFlow.map { chefResult ->
        when(chefResult){
            is UiResult.Success -> {
                ChefViewState.ChefMainInfo(chefResult.data)
            }
            else -> ChefViewState.Loading
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChefViewState.Loading
        )


}