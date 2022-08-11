package com.tyabo.data

sealed class CatalogItem {

    abstract val id: String

    data class MenuItem(
        override val id: String,
        val name: String,
        val numberPersons: NumberPersons,
        val description: String,
        val price: String,
        val menuPictureUrl: String?,
        val isHidden: Boolean
    ): CatalogItem()

    data class CollectionItem(
        override val id: String,
        val name: String,
    ): CatalogItem()
}

fun Menu.toMenuItem() = CatalogItem.MenuItem(
    id = this.id,
    name = this.name,
    numberPersons = this.numberPersons,
    description = this.description,
    price = this.price.toString(),
    menuPictureUrl = this.menuPictureUrl,
    isHidden = this.isHidden
)

fun Collection.toCollectionItem() = CatalogItem.CollectionItem(
    id = this.id,
    name = this.name,
)
