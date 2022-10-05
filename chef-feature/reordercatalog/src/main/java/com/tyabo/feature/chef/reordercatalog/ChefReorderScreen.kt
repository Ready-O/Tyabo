package com.tyabo.feature.chef.reordercatalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.feature.chef.reordercatalog.components.ReorderCollections
import com.tyabo.feature.chef.reordercatalog.components.ReorderMenus
import com.tyabo.designsystem.components.LoadingBox
import com.tyabo.feature.chef.reordercatalog.ChefReorderViewModel
import com.tyabo.feature.chef.reordercatalog.ChefReorderViewState

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
            com.tyabo.feature.chef.reordercatalog.components.ReorderMenus(
                parentCollection = menusState.parentCollection,
                menus = menusState.menus,
                moveUp = viewModel::moveUpMenu,
                moveDown = viewModel::moveDownMenu,
                navigateUp = navigateUp,
            ) { viewModel.confirmNewMenus(navigateUp) }
        }
        is ChefReorderViewState.ReorderCollections -> {
            val collectionsState = state as ChefReorderViewState.ReorderCollections
            com.tyabo.feature.chef.reordercatalog.components.ReorderCollections(
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