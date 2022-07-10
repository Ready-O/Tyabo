package com.tyabo.chef.editmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.chef.editmenu.components.EditMenuPicture
import com.tyabo.chef.editmenu.components.SelectNumberPersons
import com.tyabo.data.NumberPersons
import kotlin.time.ExperimentalTime

@Composable
fun ChefEditMenuScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    viewModel: ChefEditMenuViewModel = hiltViewModel(),
){
    val state by viewModel.editMenuState.collectAsState()

    when(state){
        EditMenuViewState.Loading -> CircularProgressIndicator()
        is EditMenuViewState.Edit -> {
            val editState = state as EditMenuViewState.Edit
            editMenuScreen(
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
                onCtaClicked = { viewModel.onCtaClicked(navigateUp) }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun editMenuScreen(
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
    onCtaClicked: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Column() {
        Box(
            modifier = Modifier.background(Color.DarkGray)
        ){
            EditMenuPicture(
                modifier = Modifier.size(150.dp),
                menuPictureUrl = menuProfileUrl,
                menuname = name,
                onClick = onPictureUpdate
            )
        }
        Row() {
            Text("Nom : ")
            TextField(value = name, onValueChange = onNameUpdate)
        }
        Column() {
            Text("Nombre de personnes : ")
            SelectNumberPersons(
                numberPersons = numberPersons,
                onNumberPersonsUpdate = onNumberPersonsUpdate
            )
        }
        Row() {
            Text("Description : ")
            TextField(value = description, onValueChange = onDescriptionUpdate)
        }
        Row() {
            Text("Prix : ")
            TextField(
                value = price,
                onValueChange = onPriceUpdate,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }
        Button(onClick = {
            keyboardController?.hide()
            onCtaClicked()
        }) {
            Text("Valider")
        }
    }
}
