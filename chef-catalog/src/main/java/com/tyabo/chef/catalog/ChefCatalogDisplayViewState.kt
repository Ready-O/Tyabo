package com.tyabo.chef.catalog

import com.tyabo.data.CatalogItem
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Collection
import com.tyabo.data.Menu

interface ChefCatalogDisplayViewState {

    object Loading: ChefCatalogDisplayViewState

    data class Catalog(
        val catalog: List<CatalogItem>,
    ): ChefCatalogDisplayViewState
}