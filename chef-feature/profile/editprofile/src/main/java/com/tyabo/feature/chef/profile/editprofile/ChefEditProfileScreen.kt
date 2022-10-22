package com.tyabo.feature.chef.profile.editprofile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.designsystem.components.LoadingBox
import com.tyabo.feature.chef.profile.editprofile.components.*
import com.tyabo.feature.chef.profile.editprofile.components.TopBar

@Composable
fun ChefEditProfileScreen(
    viewModel: ChefEditProfileViewModel = hiltViewModel(),
    navigateUp: () -> Unit
){

    val state by viewModel.editViewState.collectAsState()

    when(state){
        EditProfileViewState.Loading -> LoadingBox()
        is EditProfileViewState.Edit -> {
            val editState = state as EditProfileViewState.Edit
            EditProfileScreen(
                name = editState.name,
                phoneNumber = editState.phoneNumber,
                chefPictureUrl = editState.chefPictureUrl,
                businessPictureUrl = editState.businessPictureUrl,
                bio = editState.bio,
                onNameUpdate = viewModel::onNameUpdate,
                onPhoneNumberUpdate = viewModel::onPhoneUpdate,
                onChefPictureUpdate = viewModel::onChefPictureUpdate,
                onBusinessPictureUpdate = viewModel::onBusinessPictureUpdate,
                onBioUpdate = viewModel::onBioUpdate,
                navigateUp = navigateUp
            ) { viewModel.onCtaClicked(navigateUp) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileScreen(
    name: String,
    phoneNumber: String,
    chefPictureUrl: String?,
    businessPictureUrl: String?,
    bio: String,
    onNameUpdate: (String) -> Unit,
    onPhoneNumberUpdate: (String) -> Unit,
    onChefPictureUpdate: (String?) -> Unit,
    onBusinessPictureUpdate: (String?) -> Unit,
    onBioUpdate: (String) -> Unit,
    navigateUp: () -> Unit,
    onCtaClicked: () -> Unit
){
    Scaffold(
        topBar = {
            TopBar(
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
            BusinessPicture(
                businessProfileUrl = businessPictureUrl,
                onPictureUpdate = onBusinessPictureUpdate
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ChefPicture(
                    chefProfileUrl = chefPictureUrl,
                    onPictureUpdate = onChefPictureUpdate
                )
            }
            NameInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                name = name,
                onNameUpdate = onNameUpdate
            )
            PhoneInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                phone = phoneNumber,
                onPhoneUpdate = onPhoneNumberUpdate
            )
            BioInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                bio = bio,
                onBioUpdate = onBioUpdate
            )
        }
    }
}