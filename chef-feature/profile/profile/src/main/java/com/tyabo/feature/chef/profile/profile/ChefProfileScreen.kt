package com.tyabo.feature.chef.profile.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyabo.designsystem.components.LoadingBox
import com.tyabo.feature.chef.profile.profile.components.ProfileHeader

@Composable
fun ChefProfileScreen(
    viewModel: ChefProfileViewModel = hiltViewModel(),
    modifier: Modifier
){

    LaunchedEffect(Unit){
        viewModel.fetchProfileFirstTime()
    }

    val state by viewModel.chefState.collectAsState()

    when(state){
        is ChefViewState.Loading -> LoadingBox()
        is ChefViewState.ChefMainInfo -> {
            val chef = (state as ChefViewState.ChefMainInfo).chef
            Column(modifier = modifier) {
                ProfileHeader(
                    name = chef.name,
                    chefPictureUrl = chef.chefPictureUrl,
                    bannerPictureUrl = chef.bannerPictureUrl,
                    bio = chef.bio
                )
            }
        }
    }
}