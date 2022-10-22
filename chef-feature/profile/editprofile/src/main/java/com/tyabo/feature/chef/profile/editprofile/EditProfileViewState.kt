package com.tyabo.feature.chef.profile.editprofile

sealed interface EditProfileViewState {

    object Loading: EditProfileViewState

    data class Edit(
        val name: String,
        val phoneNumber: String,
        val chefPictureUrl: String?,
        val businessPictureUrl: String?,
        val bio: String,
    ): EditProfileViewState
}