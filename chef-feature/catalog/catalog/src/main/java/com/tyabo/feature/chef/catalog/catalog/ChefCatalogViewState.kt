package com.tyabo.feature.chef.catalog.catalog

import com.tyabo.data.CatalogItem

sealed interface ChefCatalogViewState {

    object Loading: ChefCatalogViewState

    data class Catalog(
        val catalog: List<CatalogItem>,
    ): ChefCatalogViewState
}