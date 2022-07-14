package com.tyabo.service.firebase.implemetations

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tyabo.data.CatalogItemType
import com.tyabo.data.CatalogOrder
import com.tyabo.data.UserType
import com.tyabo.service.firebase.di.CollectionReferences.CHEFS
import com.tyabo.service.firebase.di.CollectionReferences.ORDER
import com.tyabo.service.firebase.di.CollectionReferences.RESTAURANTS
import com.tyabo.service.firebase.interfaces.CatalogOrderDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class CatalogOrderDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CatalogOrderDataSource {

    override suspend fun updateCatalogOrder(
        catalogOrderId: String,
        catalogOrder: List<CatalogOrder>,
        userType: UserType,
        userId: String
    ): Result<Unit> {
        return try {
            val orderRef: CollectionReference = when (userType){
                UserType.Chef -> firestore.collection("$CHEFS/$userId/$ORDER")
                UserType.Restaurant -> firestore.collection("$RESTAURANTS/$userId/$ORDER")
                UserType.Client -> return Result.failure<Unit>(Exception())
            }
            orderRef.document(catalogOrderId).set(catalogOrder.toRemote()).await()
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun fetchCatalogOrder(
        catalogOrderId: String,
        userType: UserType,
        userId: String
    ): Result<List<CatalogOrder>> {
        return try {
            val orderRef: CollectionReference = when (userType){
                UserType.Chef -> firestore.collection("$CHEFS/$userId/$ORDER")
                UserType.Restaurant -> firestore.collection("$RESTAURANTS/$userId/$ORDER")
                UserType.Client -> return Result.failure(Exception())
            }
            val orderSnapshot = orderRef.document(catalogOrderId).get().await()
            val remoteOrder = orderSnapshot.toObject(RemoteList::class.java)
            if (remoteOrder == null) {
                Result.failure(Exception())
            } else {
                Result.success(remoteOrder.toCatalog())
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    private data class RemoteList(
        var catalogOrder: List<RemoteCatalog> = listOf()
    )

    private data class RemoteCatalog(
        var id: String = "",
        var catalogItemType: CatalogItemType = CatalogItemType.COLLECTION
    )

    private fun RemoteList.toCatalog(): List<CatalogOrder> {
        val list = mutableListOf<CatalogOrder>()
        this.catalogOrder.forEach {
            list.add(CatalogOrder(
                id = it.id,
                catalogItemType = it.catalogItemType
            ))
        }
        return list
    }

    private fun List<CatalogOrder>.toRemote(): RemoteList{
        val list = mutableListOf<RemoteCatalog>()
        this.forEach {
            list.add(RemoteCatalog(
                id = it.id,
                catalogItemType = it.catalogItemType
            ))
        }
        return RemoteList(list)
    }
}