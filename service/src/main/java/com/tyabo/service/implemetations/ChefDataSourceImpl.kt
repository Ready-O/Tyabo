package com.tyabo.service.implemetations

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.tyabo.data.Chef
import com.tyabo.service.interfaces.ChefDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class ChefDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChefDataSource {

    override fun addChef(chef: Chef) {
        try {
            firestore.collection("chefs").document(chef.id).set(chef)
        }
        catch (e: Exception){

        }
    }

    override suspend fun fetchChef(chefId: String): Result<Chef> {
        val snapshot = firestore.collection("chefs").document(chefId).get().await()
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