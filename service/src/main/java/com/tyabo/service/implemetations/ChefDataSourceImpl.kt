package com.tyabo.service.implemetations

import android.app.RemoteAction
import com.google.firebase.firestore.CollectionReference
import com.tyabo.data.CatalogItemType
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Chef
import com.tyabo.service.interfaces.ChefDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class ChefDataSourceImpl @Inject constructor(
    private val chefsCollection: CollectionReference
) : ChefDataSource {

    override fun updateChef(chef: Chef): Result<Unit> {
        return try {
            chefsCollection.document(chef.id).set(chef)
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun fetchChef(chefId: String): Result<Chef> {
        return try {
            val snapshot = chefsCollection.document(chefId).get().await()
            val chef = snapshot?.toObject(RemoteChef::class.java)
            if (chef == null) {
                Result.failure(Exception())
            } else {
                Result.success(chef.toChef())
            }
        }
        catch(e: Exception){
            Result.failure(Exception())
        }
    }

    private data class RemoteChef(
        var id: String = "",
        var name: String = "",
        var catalogOrder: List<RemoteCatalog> = mutableListOf()
    )

    private fun RemoteChef.toChef() = Chef(
        id = this.id,
        name = this.name,
        catalogOrder = this.catalogOrder.toCatalog()
    )

    private data class RemoteCatalog(
        var id: String = "",
        var catalogItemType: CatalogItemType = CatalogItemType.COLLECTION
    )

    private fun List<RemoteCatalog>.toCatalog(): List<CatalogOrder> {
        val list = mutableListOf<CatalogOrder>()
        this.forEach {
            list.add(CatalogOrder(
                id = it.id,
                catalogItemType = it.catalogItemType
            ))
        }
        return list
    }
}