package com.tyabo.chef.editmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChefEditMenuScreen(
    modifier: Modifier = Modifier,
    viewModel: ChefEditMenuViewModel = hiltViewModel(),
){
    val state by viewModel.editMenuState.collectAsState()

    when(state){
        EditMenuViewState.Loading -> CircularProgressIndicator()
        is EditMenuViewState.Edit -> {
            val editState = state as EditMenuViewState.Edit
            Column() {
                Row(){
                    Text("Nom : ")
                    TextField(value = editState.name, onValueChange = viewModel::onNameUpdate)
                }
                Row(){
                    Text("Nombre de personnes : ")
                    TextField(value = editState.numberPersons, onValueChange = viewModel::onNumberPersonsUpdate)
                }
                Row(){
                    Text("Description : ")
                    TextField(value = editState.description, onValueChange = viewModel::onDescriptionUpdate)
                }
                Row(){
                    Text("Prix : ")
                    TextField(value = editState.price.toString(), onValueChange = {})
                }
                Button(onClick = {}){
                    Text("Valider")
                }
            }
        }
    }
}