package com.reda.chef.reordercatalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.reda.chef.reordercatalog.components.ReorderCollections
import com.reda.chef.reordercatalog.components.ReorderMenus
import com.tyabo.designsystem.components.LoadingBox

@Composable
fun ChefReorderScreen(
    navigateUp: () -> Unit,
    viewModel: ChefReorderViewModel = hiltViewModel(),
){

    val state by viewModel.viewState.collectAsState()
    when(state){
        is ChefReorderViewState.Loading -> LoadingBox()
        is ChefReorderViewState.ReorderMenus -> {
            val menusState = state as ChefReorderViewState.ReorderMenus
            ReorderMenus(
                parentCollection = menusState.parentCollection,
                menus = menusState.menus,
                moveUp = viewModel::moveUpMenu,
                moveDown = viewModel::moveDownMenu,
                navigateUp = navigateUp,
            ) { viewModel.confirmNewMenus(navigateUp) }
        }
        is ChefReorderViewState.ReorderCollections -> {
            val collectionsState = state as ChefReorderViewState.ReorderCollections
            ReorderCollections(
                collections = collectionsState.collections,
                moveUp = viewModel::moveUpCollection,
                moveDown = viewModel::moveDownCollection,
                navigateUp = navigateUp,
                confirmNewOrder = { viewModel.confirmNewCollections(navigateUp) }
            )
        }
        is ChefReorderViewState.Error -> {}
    }
}