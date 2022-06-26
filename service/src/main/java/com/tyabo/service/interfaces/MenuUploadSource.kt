package com.tyabo.service.interfaces

import com.tyabo.data.UserType

interface MenuUploadSource {
    suspend fun uploadMenuPicture(
        menuId: String,
        pictureUrl: String,
        userType: UserType
    ): Result<String>
}