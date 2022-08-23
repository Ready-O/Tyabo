package com.tyabo.chef.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.repository.interfaces.ChefCatalogRepository
import com.tyabo.repository.interfaces.ChefRepository
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

    val fetchedCatalog: StateFlow<Unit> = chefRepository.catalog.map { catalogResult ->
        when(catalogResult){
            is UiResult.Success -> {
                _catalogDisplayState.value = ChefCatalogDisplayViewState.Catalog(catalog = catalogResult.data)
            }
            else -> {
                _catalogDisplayState.value = ChefCatalogDisplayViewState.Loading
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            initialValue = Unit
        )

    private val _catalogDisplayState = MutableStateFlow<ChefCatalogDisplayViewState>(
        ChefCatalogDisplayViewState.Loading
    )
    val catalogDisplayState = _catalogDisplayState.asStateFlow()

    private val _catalogState = MutableStateFlow<ChefCatalogViewState>(ChefCatalogViewState.DisplayCatalog)

    val catalogState = _catalogState.asStateFlow()

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

    private fun addCollectionState() = catalogState.value as? ChefCatalogViewState.AddCollection ?: ChefCatalogViewState.AddCollection(
        collection = ""
    )

    fun onCollectionUpdate(collectionName: String){
        viewModelScope.launch{
            _catalogState.value = addCollectionState().copy(collection = collectionName)
        }
    }

    fun addNewCollection(collectionName: String){
        viewModelScope.launch{
            chefRepository.addCollection(
                collectionName = collectionName,
                userId = userId
            )
        }
    }

    fun editCollection(collection: CatalogItem.CollectionItem){
        viewModelScope.launch{
            chefRepository.editCollection(
                collectionId = collection.id,
                collectionName = collection.name,
                userId = userId
            )
        }
    }
}
