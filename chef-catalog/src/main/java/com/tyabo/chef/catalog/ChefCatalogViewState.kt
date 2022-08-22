package com.tyabo.chef.catalog

import com.tyabo.data.CatalogItem
import kotlinx.coroutines.flow.StateFlow

interface ChefCatalogViewState {

    data class DisplayCatalog(
        val updatedCatalog: List<CatalogItem>
    ) : ChefCatalogViewState

    data class AddCollection(
        val collection: String
    ) : ChefCatalogViewState

    data class MoveMenus(
        val parentCollection: CatalogItem.CollectionItem,
        val catalogToOrder: List<CatalogItem.MenuItem>
    ) : ChefCatalogViewState
}