package com.tyabo.chef.editmenu

import com.tyabo.data.NumberPersons

interface EditMenuViewState {

    object Loading: EditMenuViewState

    data class Ready(
        val name: String,
        val numberPersons: NumberPersons,
        val description: String,
        val price: Long
    ): EditMenuViewState

    data class Edit(
        val name: String,
        val numberPersons: NumberPersons,
        val description: String,
        val price: Long
    ): EditMenuViewState
}