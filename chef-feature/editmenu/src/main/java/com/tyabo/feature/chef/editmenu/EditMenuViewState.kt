package com.tyabo.feature.chef.editmenu

import com.tyabo.data.NumberPersons

sealed interface EditMenuViewState {

    object Loading: EditMenuViewState

    data class Edit(
        val name: String,
        val numberPersons: NumberPersons,
        val description: String,
        val price: String,
        val menuPictureUrl: String?,
        val menuVideoUrl: String?
    ): EditMenuViewState

    data class Youtube(
        val url: String,
        val savedEditState: Edit,
    ): EditMenuViewState
}