package com.reda.chef.reordercatalog

import com.tyabo.data.CatalogItem

sealed interface ChefReorderViewState{

    object Loading: ChefReorderViewState

    data class ReorderMenus(
        val parentCollection: CatalogItem.CollectionItem,
        val menus: List<CatalogItem.MenuItem>
    ): ChefReorderViewState

    data class ReorderCollections(
        val collections: List<CatalogItem.CollectionItem>
    ): ChefReorderViewState

    object Error: ChefReorderViewState
}