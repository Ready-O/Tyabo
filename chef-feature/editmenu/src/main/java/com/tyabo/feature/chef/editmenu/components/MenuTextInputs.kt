package com.tyabo.feature.chef.editmenu.components

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuName(
   name: String,
   onNameUpdate: (String) -> Unit,
   modifier: Modifier = Modifier
){
    OutlinedTextField(
        modifier = modifier,
        value = name,
        onValueChange = onNameUpdate,
        label = { Text("Name*") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuPrice(
    modifier: Modifier = Modifier,
    price: String,
    onPriceUpdate: (String) -> Unit
){
    OutlinedTextField(
        modifier = modifier,
        value = price,
        onValueChange = onPriceUpdate,
        label = { Text("Price*") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuDescription(
    modifier: Modifier = Modifier,
    description: String,
    onDescriptionUpdate: (String) -> Unit
){
    OutlinedTextField(
        modifier = modifier.defaultMinSize(minHeight = 100.dp),
        value = description,
        onValueChange = onDescriptionUpdate,
        label = { Text("Description") },
    )
}