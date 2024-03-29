package com.tyabo.feature.chef.catalog.catalog

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.designsystem.components.LoadingBox
import com.tyabo.feature.chef.catalog.catalog.components.ChefCatalog

@Composable
fun ChefCatalogScreen(
    viewModel: ChefCatalogViewModel = hiltViewModel(),
    modifier: Modifier,
    navigateToEditMenu: (String?,Int?) -> Unit,
    navigateToEditCollection: (String?,String?,Boolean) -> Unit,
    navigateToReorderCatalog: (String) -> Unit
) {

    LaunchedEffect(Unit){
        viewModel.fetchOrderFirstTime()
    }

    val catalogState by viewModel.fetchedCatalog.collectAsState()

    when (catalogState) {
        is ChefCatalogViewState.Loading -> LoadingBox()
        is ChefCatalogViewState.Catalog -> {
            val state = catalogState as ChefCatalogViewState.Catalog
            ChefCatalog(
                modifier = modifier.padding(horizontal = 16.dp),
                itemsList = state.catalog,
                addMenu = { navigateToEditMenu(null,it) },
                editMenu = { navigateToEditMenu(it,null) },
                moveMenu = navigateToReorderCatalog,
                hideMenu = viewModel::hideMenu,
                unhideMenu = viewModel::unhideMenu,
                deleteMenu = viewModel::deleteMenu,
                addCollection = { navigateToEditCollection(null,null,true) },
                editCollection = { navigateToEditCollection(it.id,it.name,false) },
                moveCollection = navigateToReorderCatalog,
                deleteCollection = viewModel::deleteCollection
            )
        }
    }
}