package com.tyabo.service.implemetations

import com.google.firebase.firestore.CollectionReference
import com.tyabo.data.Chef
import com.tyabo.service.interfaces.ChefDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ChefDataSourceImpl @Inject constructor(
    private val chefsCollection: CollectionReference
) : ChefDataSource {

    override fun addChef(chef: Chef): Result<Unit> {
        return try {
            chefsCollection.document(chef.id).set(chef)
            Result.success(Unit)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun fetchChef(chefId: String): Result<Chef> {
        val snapshot = chefsCollection.document(chefId).get().await()
        val chef = snapshot?.toObject(RemoteChef::class.java)
        return if (chef == null) {
            Result.failure(Exception())
        } else {
            Result.success(chef.toChef())
        }
    }

    private data class RemoteChef(
        var id: String = "",
        var name: String = ""
    )

    private fun RemoteChef.toChef() = Chef(
        id = this.id,
        name = this.name
    )

}