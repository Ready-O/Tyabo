package com.tyabo.data

data class Chef(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val chefPictureUrl: String?,
    val businessPictureUrl: String?,
    val bio: String,
    val catalogOrderId: String
    )
