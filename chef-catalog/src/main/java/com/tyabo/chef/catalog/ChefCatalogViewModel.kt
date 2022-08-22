package com.tyabo.chef.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.*
import com.tyabo.repository.interfaces.ChefRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefCatalogViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chefRepository: ChefRepository
): ViewModel() {

    private val userId = userRepository.getUserId()

    fun fetchOrderFirstTime(){
        viewModelScope.launch {
            chefRepository.updateStateCatalogOrder(userId)
        }
    }

    val catalogDisplayState: StateFlow<ChefCatalogDisplayViewState> = chefRepository.catalogOrder.flatMapConcat { order ->
        val menusIds = mutableListOf<String>()
        val collectionsIds = mutableListOf<String>()
        order.forEach {
            if (it.catalogItemType == CatalogItemType.MENU){
                menusIds.add(it.id)
            }
            else {
                collectionsIds.add(it.id)
            }
        }
        combine(
            chefRepository.getMenus(chefId = userId, menusIds = menusIds),
            chefRepository.getCollections(chefId = userId, collectionsIds = collectionsIds)
        ){ menusResult,collectionsResult ->
            if (menusResult is UiResult.Success && collectionsResult is UiResult.Success){
                val catalog = mutableListOf<CatalogItem>()
                menusResult.data.forEach {
                    catalog.add(it.toMenuItem())
                }
                collectionsResult.data.forEach {
                    catalog.add(it.toCollectionItem())
                }
                ChefCatalogDisplayViewState.Catalog(catalog = catalog, order = order.toMutableList())
            }
            else if (menusResult is UiResult.Failure || collectionsResult is UiResult.Failure){
                ChefCatalogDisplayViewState.Loading
            }
            else {
                ChefCatalogDisplayViewState.Loading
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(0),
            initialValue = ChefCatalogDisplayViewState.Loading
        )

    private val _catalogState = MutableStateFlow<ChefCatalogViewState>(
        ChefCatalogViewState.DisplayCatalog(mutableListOf())
    )

    val catalogState = _catalogState.asStateFlow()

    fun updateCatalog(updatedCatalog: List<CatalogItem>){
        _catalogState.value = ChefCatalogViewState.DisplayCatalog(updatedCatalog)
    }

    // Editing items does not change the order value, so new items are not fetched
    // Must change MANUALLY
    private fun editItemCatalog(newItem: CatalogItem) {
        val state = catalogState.value as ChefCatalogViewState.DisplayCatalog
        val index = state.updatedCatalog.indexOfFirst { it.id == newItem.id }
        val updatedCatalog = state.updatedCatalog.toMutableList()
        updatedCatalog[index] = newItem
        _catalogState.value = ChefCatalogViewState.DisplayCatalog(updatedCatalog)
    }

    // Adding, Moving, Deleting cause order change so we force fetching
    private fun fetchCatalog(){
        _catalogState.value = ChefCatalogViewState.DisplayCatalog(listOf())
    }

    fun hideMenu(menu: CatalogItem.MenuItem){
        viewModelScope.launch{
            chefRepository.changeHideMenu(
                menuId = menu.id,
                isHidden = true,
                userId = userId
            )
            val newItem = menu.copy(isHidden = true)
            editItemCatalog(newItem)
        }
    }

    fun unhideMenu(menu: CatalogItem.MenuItem){
        viewModelScope.launch {
            chefRepository.changeHideMenu(
                menuId = menu.id,
                isHidden = false,
                userId = userId
            )
            val newItem = menu.copy(isHidden = false)
            editItemCatalog(newItem)
        }
    }

    fun deleteMenu(menuId: String){
        viewModelScope.launch{
            chefRepository.deleteMenu(
                menuId = menuId,
                userId = userId
            )
            fetchCatalog()
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
            editItemCatalog(collection)
        }
    }
}
