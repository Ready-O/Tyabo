package com.tyabo.data

data class Menu(
    val id: String,
    val name: String,
    val numberPersons: NumberPersons,
    val description: String,
    val price: Double
    )

enum class NumberPersons {
    ONE,
    TWO,
    THREE,
    MORE,
}
