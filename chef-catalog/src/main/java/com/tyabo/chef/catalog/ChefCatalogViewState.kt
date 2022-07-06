package com.tyabo.chef.catalog

import kotlinx.coroutines.flow.StateFlow

interface ChefCatalogViewState {

    data class DisplayCollection(
        val catalogState: StateFlow<ChefCatalogDisplayViewState>
    ) : ChefCatalogViewState

    data class EditCollection(
        val catalogState: StateFlow<ChefCatalogDisplayViewState>,
        val collection: String
    ) : ChefCatalogViewState
}