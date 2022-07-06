package com.tyabo.chef.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.CatalogItem
import com.tyabo.data.CatalogItemType
import com.tyabo.data.toCollectionItem
import com.tyabo.data.toMenuItem
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChefCatalogDisplayViewState.Loading
        )

    private val _catalogState = MutableStateFlow<ChefCatalogViewState>(
        ChefCatalogViewState.EditCollection(catalogState = catalogDisplayState, collection = "")
    )

    val catalogState = _catalogState.asStateFlow()

    private fun editCollectionState() = catalogState.value as? ChefCatalogViewState.EditCollection ?: ChefCatalogViewState.EditCollection(
        catalogState = catalogDisplayState,
        collection = ""
    )

    fun onCollectionUpdate(collectionName: String){
        viewModelScope.launch{
            _catalogState.value = editCollectionState().copy(collection = collectionName)
        }
    }

    fun onCollectionCtaClick(collectionName: String){
        viewModelScope.launch{
            chefRepository.addCollection(
                collectionName = collectionName,
                userId = userId
            )
        }
    }
}
