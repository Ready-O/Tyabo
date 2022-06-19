package com.tyabo.chef.editmenu

interface EditMenuViewState {

    object Loading: EditMenuViewState

    data class Edit(
        val name: String,
        val numberPersons: String,
        val description: String,
        val price: Long
    ): EditMenuViewState
}