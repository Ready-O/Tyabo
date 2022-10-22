package com.tyabo.service.firebase.implemetations

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.tyabo.service.firebase.di.CollectionReferences
import com.tyabo.service.firebase.interfaces.ChefUploadDataSource
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ChefUploadDataSourceImpl @Inject constructor(
    private val storageRef: StorageReference
): ChefUploadDataSource {

    override suspend fun uploadChefPicture(userId: String, pictureUrl: String): Result<String> {
        return try {
            val path = "${CollectionReferences.CHEFS}/$userId/${PROFILE_PICTURES}/${CHEF_PICTURE_FILE}"
            val uri = Uri.parse(pictureUrl)
            storageRef.child(path).putFile(uri).await()
            val downloadUrl = storageRef.child(path).downloadUrl.await().toString()
            Result.success(downloadUrl)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteChefPicture(userId: String) {
        try {
            val path = "${CollectionReferences.CHEFS}/$userId/${PROFILE_PICTURES}/${CHEF_PICTURE_FILE}"
            storageRef.child(path).delete().await()
        }
        catch (e: Exception){
        }
    }

    override suspend fun uploadBusinessPicture(userId: String, pictureUrl: String): Result<String> {
        return try {
            val path = "${CollectionReferences.CHEFS}/$userId/${PROFILE_PICTURES}/${BUSINESS_PICTURE_FILE}"
            val uri = Uri.parse(pictureUrl)
            storageRef.child(path).putFile(uri).await()
            val downloadUrl = storageRef.child(path).downloadUrl.await().toString()
            Result.success(downloadUrl)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteBusinessPicture(userId: String) {
        try {
            val path = "${CollectionReferences.CHEFS}/$userId/${PROFILE_PICTURES}/${BUSINESS_PICTURE_FILE}"
            storageRef.child(path).delete().await()
        }
        catch (e: Exception){
        }
    }

    companion object {
        private const val PROFILE_PICTURES = "profile_pictures"
        private const val CHEF_PICTURE_FILE = "chef_picture.jpg"
        private const val BUSINESS_PICTURE_FILE = "business_picture.jpg"
    }
}