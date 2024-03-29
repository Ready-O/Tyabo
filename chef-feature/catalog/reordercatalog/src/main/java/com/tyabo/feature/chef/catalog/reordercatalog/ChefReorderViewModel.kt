package com.tyabo.feature.chef.catalog.reordercatalog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyabo.common.UiResult
import com.tyabo.data.CatalogItem
import com.tyabo.data.extractMenusOfCollection
import com.tyabo.repository.interfaces.ChefCatalogRepository
import com.tyabo.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefReorderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val chefRepository: ChefCatalogRepository
): ViewModel() {

    private val userId = userRepository.getUserId()
    val catalogItemId: String? = savedStateHandle["catalogItemId"]

    private val _viewState = MutableStateFlow<ChefReorderViewState>(ChefReorderViewState.Loading)
    val viewState: StateFlow<ChefReorderViewState> = _viewState.asStateFlow()

    private val catalogItems = mutableListOf<CatalogItem>()

    init {
        viewModelScope.launch {
            if (catalogItemId != null){
                chefRepository.catalog.collectLatest { result ->
                    when(result){
                        is UiResult.Success -> {
                            catalogItems.addAll(result.data)
                            _viewState.value = result.data.reorderSetup(catalogItemId)
                        }
                        is UiResult.Loading -> ChefReorderViewState.Loading
                        is UiResult.Failure -> ChefReorderViewState.Error
                    }
                }
            }
            else{
                _viewState.value = ChefReorderViewState.Error
            }
        }
    }

    private fun List<CatalogItem>.reorderSetup(catalogItemId: String): ChefReorderViewState {
        val catalogItem = this.first { it.id == catalogItemId }
        val index = this.indexOfFirst { it.id == catalogItemId }
        return when (catalogItem){
            is CatalogItem.MenuItem -> this.reorderMenusSetup(index)
            else -> this.reorderCollectionsSetup()
        }
    }

    // Reorder Menus

    private fun List<CatalogItem>.reorderMenusSetup(menuIndex: Int): ChefReorderViewState {
        var indexParentCollection = menuIndex
        while (this[indexParentCollection] !is CatalogItem.CollectionItem){
            indexParentCollection --
        }
        val parentCollection = this[indexParentCollection]
        val menusResult = this.extractMenusOfCollection(indexParentCollection)
        return if (parentCollection is CatalogItem.CollectionItem && menusResult.isSuccess){
            ChefReorderViewState.ReorderMenus(
                parentCollection = parentCollection,
                menus = menusResult.getOrThrow()
            )
        }
        else{
            ChefReorderViewState.Error
        }
    }

    fun moveUpMenu(menuIndex: Int){
        viewModelScope.launch {
            val menus = (viewState.value as ChefReorderViewState.ReorderMenus).menus
            val newMenus = menus.toMutableList()
            newMenus[menuIndex-1] = menus[menuIndex]
            newMenus[menuIndex] = menus[menuIndex-1]
            _viewState.value = (viewState.value as ChefReorderViewState.ReorderMenus).copy(menus = newMenus)
        }
    }

    fun moveDownMenu(menuIndex: Int){
        viewModelScope.launch {
            val menus = (viewState.value as ChefReorderViewState.ReorderMenus).menus
            val newMenus = menus.toMutableList()
            newMenus[menuIndex+1] = menus[menuIndex]
            newMenus[menuIndex] = menus[menuIndex+1]
            _viewState.value = (viewState.value as ChefReorderViewState.ReorderMenus).copy(menus = newMenus)
        }
    }

    fun confirmNewMenus(navigateUp: () -> Unit){
        viewModelScope.launch {
            val state = viewState.value as ChefReorderViewState.ReorderMenus
            val indexParentCollection = catalogItems.indexOfFirst { it.id == state.parentCollection.id }
            val newOrder = catalogItems.toMutableList()
            for (index in (0 until state.menus.size)){
                newOrder[indexParentCollection+1+index] = state.menus[index]
            }
            chefRepository.editCatalogOrder(
                userId = userId,
                newCatalog = newOrder
            )
            navigateUp()
        }
    }

    // Reorder Collections
    private fun List<CatalogItem>.reorderCollectionsSetup(): ChefReorderViewState {
        val collections = mutableListOf<CatalogItem.CollectionItem>()
        this.forEach { item ->
            if(item is CatalogItem.CollectionItem){
                collections.add(item)
            }
        }
        return ChefReorderViewState.ReorderCollections(collections = collections)
    }

    fun moveUpCollection(collectionIndex: Int){
        viewModelScope.launch {
            val collections = (viewState.value as ChefReorderViewState.ReorderCollections).collections
            val newCollections = collections.toMutableList()
            newCollections[collectionIndex-1] = collections[collectionIndex]
            newCollections[collectionIndex] = collections[collectionIndex-1]
            _viewState.value = (viewState.value as ChefReorderViewState.ReorderCollections).copy(collections = newCollections)

        }
    }

    fun moveDownCollection(collectionIndex: Int){
        viewModelScope.launch {
            val collections = (viewState.value as ChefReorderViewState.ReorderCollections).collections
            val newCollections = collections.toMutableList()
            newCollections[collectionIndex+1] = collections[collectionIndex]
            newCollections[collectionIndex] = collections[collectionIndex+1]
            _viewState.value = (viewState.value as ChefReorderViewState.ReorderCollections).copy(collections = newCollections)

        }
    }

    fun confirmNewCollections(navigateUp: () -> Unit){
        viewModelScope.launch {
            val newOrder = mutableListOf<CatalogItem>()
            val collections = (viewState.value as ChefReorderViewState.ReorderCollections).collections.toMutableList()
            collections.forEach{ collection ->
                newOrder.add(collection)
                val collectionIndex = catalogItems.indexOfFirst { it.id == collection.id }
                val menusResult = catalogItems.extractMenusOfCollection(collectionIndex)
                menusResult.onSuccess {
                    newOrder.addAll(it)
                }
                    .onFailure {
                        return@launch
                    }
            }
            chefRepository.editCatalogOrder(
                userId = userId,
                newCatalog = newOrder
            )
            navigateUp()
        }
    }
}