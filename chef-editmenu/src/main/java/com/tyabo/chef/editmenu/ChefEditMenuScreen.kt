package com.tyabo.chef.editmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChefEditMenuScreen(
    modifier: Modifier = Modifier,
    viewModel: ChefEditMenuViewModel = hiltViewModel(),
){
    Column() {
        Row(){
            Text("Nom : ")
            TextField(value = "", onValueChange = {})
        }
        Row(){
            Text("Nombre de personnes : ")
            TextField(value = "", onValueChange = {})
        }
        Row(){
            Text("Description : ")
            TextField(value = "", onValueChange = {})
        }
    }
}