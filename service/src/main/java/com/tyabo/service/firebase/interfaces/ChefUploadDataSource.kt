package com.tyabo.service.firebase.interfaces

interface ChefUploadDataSource {
    suspend fun uploadChefPicture(
        userId: String,
        pictureUrl: String,
    ): Result<String>

    suspend fun deleteChefPicture(userId: String)

    suspend fun uploadBusinessPicture(
        userId: String,
        pictureUrl: String,
    ): Result<String>

    suspend fun deleteBusinessPicture(userId: String)
}