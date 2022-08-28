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

/**
 * Extract menus that follow the collection with collectionIndex in the catalog
 */
fun List<CatalogItem>.extractMenusOfCollection(collectionIndex: Int): Result<List<CatalogItem.MenuItem>>{
    if (collectionIndex == this.size - 1){
        return Result.success(listOf())
    } else{
        var index = collectionIndex + 1
        while (this[index] !is CatalogItem.CollectionItem && index != (this.size - 1)){
            index ++
        }
        if ( this[index] is CatalogItem.MenuItem && index == (this.size - 1)){ index ++ }
        val selectedList = this.subList((collectionIndex + 1),index)
        val menuList: MutableList<CatalogItem.MenuItem> = mutableListOf()
        selectedList.forEach{
            if (it is CatalogItem.MenuItem){
                menuList.add(it)
            }
            else {
                return Result.failure(Exception())
            }
        }
        return Result.success(menuList)
    }
}
