package com.tyabo.chef.editmenu

import com.tyabo.data.NumberPersons
import kotlinx.coroutines.flow.StateFlow

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
        val savedState: EditMenuViewState.Edit,
    ): EditMenuViewState
}