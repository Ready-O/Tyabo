package com.tyabo.service.implemetations

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tyabo.data.CatalogOrder
import com.tyabo.data.Collection
import com.tyabo.data.UserType
import com.tyabo.service.di.CollectionReferences.CHEFS
import com.tyabo.service.di.CollectionReferences.COLLECTIONS
import com.tyabo.service.di.CollectionReferences.RESTAURANTS
import com.tyabo.service.interfaces.CollectionDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class CollectionDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CollectionDataSource {

    override suspend fun addCollection(
        collection: Collection,
        userType: UserType,
        userId: String
    ): Result<Unit> {
        return try {
            val collectionRef: CollectionReference = when (userType){
                UserType.Chef -> firestore.collection("$CHEFS/$userId/$COLLECTIONS")
                UserType.Restaurant -> firestore.collection("$RESTAURANTS/$userId/$COLLECTIONS")
                UserType.Client -> return Result.failure<Unit>(Exception())
            }
            collectionRef.document(collection.id).set(collection).await()
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun fetchCollections(
        userType: UserType,
        userId: String,
        catalogToFetch: List<CatalogOrder>
    ): Result<List<Collection>> {
         try {
            val collectionRef: CollectionReference = when (userType){
                UserType.Chef -> firestore.collection("$CHEFS/$userId/$COLLECTIONS")
                UserType.Restaurant -> firestore.collection("$RESTAURANTS/$userId/$COLLECTIONS")
                UserType.Client -> return Result.failure(Exception())
            }
            val fetchedCollections: MutableList<Collection> = mutableListOf()
            catalogToFetch.forEach { element ->
                val collectionSnapshot = collectionRef.document(element.id).get().await()
                val remoteCollection = collectionSnapshot.toObject(RemoteCollection::class.java)
                if (remoteCollection == null){
                    return Result.failure(Exception())
                }
                else {
                    fetchedCollections.add(remoteCollection.toCollection())
                }
            }
            return Result.success(fetchedCollections)
        }
        catch (e: Exception){
            return Result.failure(e)
        }
    }

    private data class RemoteCollection(
        var id: String = "",
        var name: String = "",
    )

    private fun RemoteCollection.toCollection() = Collection(
        id = this.id,
        name = this.name,
    )
}