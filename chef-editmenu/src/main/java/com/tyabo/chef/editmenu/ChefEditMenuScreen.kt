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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.chef.editmenu.components.EditMenuPicture
import com.tyabo.data.NumberPersons

@Composable
fun ChefEditMenuScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    viewModel: ChefEditMenuViewModel = hiltViewModel(),
){
    val state by viewModel.editMenuState.collectAsState()

    when(state){
        EditMenuViewState.Loading -> CircularProgressIndicator()

        is EditMenuViewState.Ready -> {
            val editState = state as EditMenuViewState.Ready
            editMenuScreen(
                name = editState.name,
                numberPersons = editState.numberPersons,
                description = editState.description,
                price = editState.price,
                menuProfileUrl = editState.menuPictureUrl,
                navigateUp = navigateUp,
                viewModel
            )
        }

        is EditMenuViewState.Edit -> {
            val editState = state as EditMenuViewState.Edit
            editMenuScreen(
                name = editState.name,
                numberPersons = editState.numberPersons,
                description = editState.description,
                price = editState.price,
                menuProfileUrl = editState.menuPictureUrl,
                navigateUp = navigateUp,
                viewModel
            )
        }
    }
}

@Composable
private fun editMenuScreen(
    name: String,
    numberPersons: NumberPersons,
    description: String,
    price: String,
    menuProfileUrl: String?,
    navigateUp: () -> Unit,
    viewModel: ChefEditMenuViewModel
) {
    Column() {
        Box(
            modifier = Modifier.background(Color.DarkGray)
        ){
            EditMenuPicture(
                modifier = Modifier.size(150.dp),
                menuPictureUrl = menuProfileUrl,
                menuname = name,
                onClick = viewModel::onPictureUpdate
            )
        }
        Row() {
            Text("Nom : ")
            TextField(value = name, onValueChange = viewModel::onNameUpdate)
        }
        Column() {
            Text("Nombre de personnes : ")
            selectNumberPersons(numberPersons, viewModel)
        }
        Row() {
            Text("Description : ")
            TextField(value = description, onValueChange = viewModel::onDescriptionUpdate)
        }
        Row() {
            Text("Prix : ")
            TextField(
                value = price,
                onValueChange = viewModel::onPriceUpdate,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
        }
        Button(onClick = {
            viewModel.onCtaClicked(
                name = name,
                numberPersons = numberPersons,
                description = description,
                price = price,
                menuPictureUrl = menuProfileUrl,
                navigateUp = navigateUp
            )
        }) {
            Text("Valider")
        }
    }
}

@Composable
private fun selectNumberPersons(
    numberPersons: NumberPersons,
    viewModel: ChefEditMenuViewModel
) {
    Row(){
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.ONE,
                onClick = { viewModel.onNumberPersonsUpdate(NumberPersons.ONE) }
            )
            Text(text = "One")
        }
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.TWO,
                onClick = { viewModel.onNumberPersonsUpdate(NumberPersons.TWO) }
            )
            Text(text = "Two")
        }
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.THREE,
                onClick = { viewModel.onNumberPersonsUpdate(NumberPersons.THREE) }
            )
            Text(text = "Three")
        }
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.MORE,
                onClick = { viewModel.onNumberPersonsUpdate(NumberPersons.MORE) }
            )
            Text(text = "More")
        }
    }
}