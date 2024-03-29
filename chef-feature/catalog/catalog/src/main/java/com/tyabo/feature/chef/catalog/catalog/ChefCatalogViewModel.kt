package com.tyabo.feature.chef.catalog.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.repository.interfaces.ChefCatalogRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefCatalogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chefRepository: ChefCatalogRepository
): ViewModel() {

    private val userId = userRepository.getUserId()

    fun fetchOrderFirstTime(){
        viewModelScope.launch {
            chefRepository.updateStateCatalog(userId)
        }
    }

    val fetchedCatalog: StateFlow<ChefCatalogViewState> = chefRepository.catalog.map { catalogResult ->
        when(catalogResult){
            is UiResult.Success -> ChefCatalogViewState.Catalog(catalog = catalogResult.data)
            else -> ChefCatalogViewState.Loading
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChefCatalogViewState.Loading
        )

    fun hideMenu(menu: CatalogItem.MenuItem){
        viewModelScope.launch{
            chefRepository.changeHideMenu(
                menuId = menu.id,
                isHidden = true,
                userId = userId
            )
        }
    }

    fun unhideMenu(menu: CatalogItem.MenuItem){
        viewModelScope.launch {
            chefRepository.changeHideMenu(
                menuId = menu.id,
                isHidden = false,
                userId = userId
            )
        }
    }

    fun deleteMenu(menuId: String){
        viewModelScope.launch{
            chefRepository.deleteMenu(
                menuId = menuId,
                userId = userId
            )
        }
    }

    fun deleteCollection(collectionId: String){
        viewModelScope.launch{
            chefRepository.deleteCollection(
                collectionId = collectionId,
                userId = userId
            )
        }
    }
}
