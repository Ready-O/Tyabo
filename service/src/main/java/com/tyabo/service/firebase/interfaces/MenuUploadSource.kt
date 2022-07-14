package com.tyabo.service.firebase.interfaces

import com.tyabo.data.UserType

interface MenuUploadSource {
    suspend fun uploadMenuPicture(
        userId: String,
        menuId: String,
        pictureUrl: String,
        userType: UserType
    ): Result<String>

    suspend fun deleteMenuPicture(
        userId: String,
        menuId: String,
        userType: UserType
    )
}