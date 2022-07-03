package com.tyabo.data

data class CatalogOrder(val id: String, val catalogItemType: CatalogItemType)

enum class CatalogItemType {
    MENU,
    COLLECTION
}