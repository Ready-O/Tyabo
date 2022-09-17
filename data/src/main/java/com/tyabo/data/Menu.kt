package com.tyabo.data

data class Menu(
    val id: String,
    val name: String,
    val numberPersons: NumberPersons,
    val description: String,
    val price: Double,
    val menuPictureUrl: String?,
    val menuVideoUrl: String?,
    val isHidden: Boolean
    )

enum class NumberPersons {
    ONE,
    TWO,
    MORE,
}
