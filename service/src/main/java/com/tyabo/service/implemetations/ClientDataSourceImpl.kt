package com.tyabo.service.implemetations

import com.google.firebase.firestore.CollectionReference
import com.tyabo.data.Client
import com.tyabo.service.interfaces.ClientDataSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class ClientDataSourceImpl @Inject constructor(
    private val clientsCollection: CollectionReference
) : ClientDataSource {

    override fun addClient(client: Client) {
        try {
            clientsCollection.document(client.id).set(client)
        }
        catch (e: Exception){

        }
    }

    override suspend fun fetchClient(clientId: String): Result<Client> {
        val snapshot = clientsCollection.document(clientId).get().await()
        val client = snapshot?.toObject(RemoteClient::class.java)
        return if (client == null) {
            Result.failure(Exception())
        } else {
            Result.success(client.toClient())
        }
    }

    private data class RemoteClient(
        var id: String = "",
        var name: String = ""
    )

    private fun RemoteClient.toClient() = Client(
        id = this.id,
        name = this.name
    )

}