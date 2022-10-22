package com.tyabo.feature.chef.profile.editprofile.components


import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameInput(
    modifier: Modifier = Modifier,
    name: String,
    onNameUpdate: (String) -> Unit
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
fun PhoneInput(
    modifier: Modifier = Modifier,
    phone: String,
    onPhoneUpdate: (String) -> Unit
){
    OutlinedTextField(
        modifier = modifier,
        value = phone,
        onValueChange = onPhoneUpdate,
        label = { Text("Phone Number*") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioInput(
    modifier: Modifier = Modifier,
    bio: String,
    onBioUpdate: (String) -> Unit
){
    OutlinedTextField(
        modifier = modifier,
        value = bio,
        onValueChange = onBioUpdate,
        label = { Text("Bio") },
    )
}