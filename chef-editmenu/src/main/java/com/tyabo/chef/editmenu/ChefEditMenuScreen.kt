package com.tyabo.chef.editmenu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.chef.editmenu.components.*
import com.tyabo.data.NumberPersons
import com.tyabo.designsystem.components.LoadingBox

@Composable
fun ChefEditMenuScreen(
    navigateUp: () -> Unit,
    viewModel: ChefEditMenuViewModel = hiltViewModel(),
){
    val state by viewModel.editMenuState.collectAsState()
    val videoState by viewModel.videoState.collectAsState()

    when(state){
        EditMenuViewState.Loading -> LoadingBox()
        is EditMenuViewState.Edit -> {
            val editState = state as EditMenuViewState.Edit
            EditMenuScreen(
                name = editState.name,
                numberPersons = editState.numberPersons,
                description = editState.description,
                price = editState.price,
                menuProfileUrl = editState.menuPictureUrl,
                onPictureUpdate = viewModel::onPictureUpdate,
                onNameUpdate = viewModel::onNameUpdate,
                onNumberPersonsUpdate = viewModel::onNumberPersonsUpdate,
                onDescriptionUpdate = viewModel::onDescriptionUpdate,
                onPriceUpdate = viewModel::onPriceUpdate,
                videoState = videoState,
                addYoutubeVideo = viewModel::displayYoutubeScreen,
                deleteVideo = viewModel::deleteVideo,
                navigateUp = navigateUp,
            ) { viewModel.onCtaClicked(navigateUp) }
        }
        is EditMenuViewState.Youtube -> {
            val inputYoutubeState = state as EditMenuViewState.Youtube
            YoutubeScreen(
                youtubeUrl = inputYoutubeState.url,
                onUrlUpdate = viewModel::onVideoUrlUpdate,
                backToMain = viewModel::backToMain,
                exportUrl = viewModel::exportVideoUrl
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditMenuScreen(
    name: String,
    numberPersons: NumberPersons,
    description: String,
    price: String,
    menuProfileUrl: String?,
    onPictureUpdate: (String?) -> Unit,
    onNameUpdate: (String) -> Unit,
    onNumberPersonsUpdate: (NumberPersons) -> Unit,
    onDescriptionUpdate: (String) -> Unit,
    onPriceUpdate: (String) -> Unit,
    videoState: YoutubeVideoState,
    addYoutubeVideo: () -> Unit,
    deleteVideo: () -> Unit,
    navigateUp: () -> Unit,
    onCtaClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopBar(
                navigateUp = navigateUp,
                onCtaClicked = onCtaClicked
            )
        }
    ) { paddingForBars ->
        Column(
            modifier = Modifier
                .padding(paddingForBars) // Careful! scafoldPadding should not be applied to scollable screen
                .verticalScroll(rememberScrollState())
        ) {
            MenuPicture(menuProfileUrl, onPictureUpdate)
            MenuName(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                name = name,
                onNameUpdate = onNameUpdate,
            )
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                MenuPrice(
                    modifier = Modifier.widthIn(max = 100.dp),
                    price = price,
                    onPriceUpdate = onPriceUpdate
                )
                SelectNumberPersons(
                    modifier = Modifier.padding(start = 8.dp),
                    numberPersons = numberPersons,
                    onNumberPersonsUpdate = onNumberPersonsUpdate
                )
            }
            MenuDescription(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                description = description,
                onDescriptionUpdate = onDescriptionUpdate
            )
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            VideoScreen(
                videoState = videoState,
                addYoutubeVideo = addYoutubeVideo,
                deleteVideo = deleteVideo
            )
        }
    }
}


