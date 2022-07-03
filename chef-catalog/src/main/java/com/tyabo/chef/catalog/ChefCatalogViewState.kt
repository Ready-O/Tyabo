package com.tyabo.chef.catalog

import com.tyabo.data.CatalogOrder
import com.tyabo.data.Menu
import com.tyabo.data.NumberPersons

interface ChefCatalogViewState {

    object Loading: ChefCatalogViewState

    data class Catalog(
        val catalog: List<Menu>,
        val order: MutableList<CatalogOrder>
    ): ChefCatalogViewState
}