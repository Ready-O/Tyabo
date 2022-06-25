package com.tyabo.chef.editmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.data.NumberPersons

@Composable
fun ChefEditMenuScreen(
    modifier: Modifier = Modifier,
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
    price: Long,
    viewModel: ChefEditMenuViewModel
) {
    Column() {
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
            TextField(value = price.toString(), onValueChange = {})
        }
        Button(onClick = {
            viewModel.onCtaClicked(
                name = name,
                numberPersons = numberPersons,
                description = description
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