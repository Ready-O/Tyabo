package com.tyabo.chef.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class ChefCatalogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chefRepository: ChefRepository
): ViewModel() {

    val catalogState: StateFlow<ChefCatalogViewState> = combine(
            chefRepository.getMenus(userRepository.getUserId()),
            chefRepository.getChef(userRepository.getUserId())
        ){ menusResult, chef ->
            if (chef.isSuccess){
                when (menusResult){
                    is UiResult.Success -> ChefCatalogViewState.Catalog(
                        catalog = menusResult.data, order = chef.getOrNull()!!.catalogOrder.toMutableList())
                    is UiResult.Failure -> ChefCatalogViewState.Loading
                    is UiResult.Loading -> ChefCatalogViewState.Loading
                }
            } else {
                ChefCatalogViewState.Loading
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeout = Duration.ZERO, replayExpiration = Duration.ZERO),
            initialValue = ChefCatalogViewState.Loading
        )
}
