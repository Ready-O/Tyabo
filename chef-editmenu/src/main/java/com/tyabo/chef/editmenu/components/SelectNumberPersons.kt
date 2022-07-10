package com.tyabo.chef.editmenu.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.tyabo.data.NumberPersons


@Composable
fun SelectNumberPersons(
    numberPersons: NumberPersons,
    onNumberPersonsUpdate: (NumberPersons) -> Unit
) {
    Row(){
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.ONE,
                onClick = { onNumberPersonsUpdate(NumberPersons.ONE) }
            )
            Text(text = "One")
        }
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.TWO,
                onClick = { onNumberPersonsUpdate(NumberPersons.TWO) }
            )
            Text(text = "Two")
        }
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.THREE,
                onClick = { onNumberPersonsUpdate(NumberPersons.THREE) }
            )
            Text(text = "Three")
        }
        Row(){
            RadioButton(
                selected = numberPersons == NumberPersons.MORE,
                onClick = { onNumberPersonsUpdate(NumberPersons.MORE) }
            )
            Text(text = "More")
        }
    }
}