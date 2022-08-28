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

/**
 * Extract menus that follow the collection with collectionIndex in the catalog
 */
fun List<CatalogOrder>.extractMenusOfCollection(collectionIndex: Int): Result<List<CatalogOrder>>{
    if (collectionIndex == this.size - 1){
        return Result.success(listOf())
    } else{
        var index = collectionIndex + 1
        while (this[index].catalogItemType != CatalogItemType.COLLECTION && index != (this.size - 1)){
            index ++
        }
        if ( this[index].catalogItemType == CatalogItemType.MENU && index == (this.size - 1)){ index ++ }
        val selectedList = this.subList((collectionIndex + 1),index)
        val menuList: MutableList<CatalogOrder> = mutableListOf()
        selectedList.forEach{
            if (it.catalogItemType == CatalogItemType.MENU){
                menuList.add(it)
            }
            else {
                return Result.failure(Exception())
            }
        }
        return Result.success(menuList)
    }
}