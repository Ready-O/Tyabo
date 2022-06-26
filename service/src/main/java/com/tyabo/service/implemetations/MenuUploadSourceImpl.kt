package com.tyabo.service.implemetations

import android.net.Uri
import com.google.firebase.storage.StorageReference
import com.tyabo.data.UserType
import com.tyabo.service.di.CollectionReferences
import com.tyabo.service.interfaces.MenuUploadSource
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class MenuUploadSourceImpl@Inject constructor(
    private val storageRef: StorageReference
) : MenuUploadSource {

    override suspend fun uploadMenuPicture(menuId: String, pictureUrl: String, userType: UserType): Result<String> {
        return try {
            val path = when (userType){
                UserType.Chef ->
                    "${CollectionReferences.CHEFS}/${CollectionReferences.MENUS}/${MENUS_PICTURES}/$menuId.jpg"
                UserType.Restaurant ->
                    "${CollectionReferences.RESTAURANTS}/${CollectionReferences.MENUS}/${MENUS_PICTURES}/$menuId.jpg"
                UserType.Client -> return Result.failure<String>(Exception())
            }

            val uri = Uri.parse(pictureUrl)

            val storageTask = storageRef.child(path).putFile(uri).await()
            val downloadUrl = storageTask.task.result.toString()
            Result.success(downloadUrl)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    companion object {
        private const val MENUS_PICTURES = "menus_pictures"
    }
}