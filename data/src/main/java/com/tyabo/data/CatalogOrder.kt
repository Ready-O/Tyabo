package com.tyabo.data

data class CatalogOrder(val id: String, val catalogItemType: CatalogItemType)

enum class CatalogItemType {
    MENU,
    COLLECTION
}

fun CatalogItem.toCatalogOrder(): CatalogOrder{
    return when(this){
        is CatalogItem.MenuItem -> CatalogOrder(id = this.id, catalogItemType = CatalogItemType.MENU)
        is CatalogItem.CollectionItem -> CatalogOrder(id = this.id, catalogItemType = CatalogItemType.COLLECTION)
    }
}