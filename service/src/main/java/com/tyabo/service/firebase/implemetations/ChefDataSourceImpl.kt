package com.tyabo.service.firebase.implemetations

import com.google.firebase.firestore.CollectionReference
import com.tyabo.data.Chef
import com.tyabo.service.firebase.interfaces.ChefDataSource
import kotlinx.coroutines.tasks.await
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
        var phoneNumber: String = "",
        var chefPictureUrl: String? = null,
        var businessPictureUrl: String? = null,
        var bio: String = "",
        val catalogOrderId: String = ""
    )

    private fun RemoteChef.toChef() = Chef(
        id = this.id,
        name = this.name,
        phoneNumber = this.phoneNumber,
        chefPictureUrl = this.chefPictureUrl,
        businessPictureUrl = this.businessPictureUrl,
        bio = this.bio,
        catalogOrderId = this.catalogOrderId
    )

}