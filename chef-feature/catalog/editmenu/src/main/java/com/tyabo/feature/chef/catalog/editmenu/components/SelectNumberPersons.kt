package com.tyabo.feature.chef.catalog.editmenu.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tyabo.data.NumberPersons


@ExperimentalMaterial3Api
@Composable
fun SelectNumberPersons(
    modifier: Modifier = Modifier,
    numberPersons: NumberPersons,
    onNumberPersonsUpdate: (NumberPersons) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Select number of persons :",
            style = MaterialTheme.typography.bodySmall
        )
        Row {
            FilterChip(
                selected = numberPersons == NumberPersons.ONE,
                label = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = null
                    )
                }
            ) { onNumberPersonsUpdate(NumberPersons.ONE) }
            FilterChip(
                selected = numberPersons == NumberPersons.TWO,
                label = {
                    Row() {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null
                        )
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null
                        )
                    }
                }
            ) { onNumberPersonsUpdate(NumberPersons.TWO) }
            FilterChip(
                selected = numberPersons == NumberPersons.MORE,
                label = {
                    Row() {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null
                        )
                        Text("3+")
                    }
                }
            ) { onNumberPersonsUpdate(NumberPersons.MORE) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChip(
    selected: Boolean,
    label: @Composable () -> Unit,
    onClick: () -> Unit
){
    FilterChip(
        modifier = Modifier.padding(horizontal = 4.dp),
        leadingIcon = if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null
                    )
                }
            }
            else{null},
        label = label,
        selected = selected,
        onClick = onClick
    )
}