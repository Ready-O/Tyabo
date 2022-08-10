package com.tyabo.chef.catalog

import kotlinx.coroutines.flow.StateFlow

interface ChefCatalogViewState {

    object DisplayCatalog : ChefCatalogViewState

    data class AddCollection(
        val collection: String
    ) : ChefCatalogViewState
}