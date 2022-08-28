package com.tyabo.chef.catalog

import com.tyabo.data.CatalogItem
import kotlinx.coroutines.flow.StateFlow

sealed interface ChefCatalogViewState {

    object DisplayCatalog: ChefCatalogViewState

    data class AddCollection(
        val collection: String
    ) : ChefCatalogViewState
}