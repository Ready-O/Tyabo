package com.tyabo.feature.chef.profile.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tyabo.designsystem.picture.StandardImage

@Composable
fun ProfileHeader(
    name: String,
    chefPictureUrl: String?,
    bannerPictureUrl: String?,
    bio: String?,
){
  Column {
      ChefBanner(bannerUrl = bannerPictureUrl)
      Row(modifier = Modifier.padding(16.dp)){
          ChefPicture(chefPictureUrl)
          Text(
              modifier = Modifier.padding(start = 8.dp),
              text = name
          )
      }
      if (bio != null){
          Text(
              modifier = Modifier.padding(16.dp),
              text = bio
          )
      }
  }
}

@Composable
private fun ChefBanner(bannerUrl: String?){
    StandardImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        url = bannerUrl,
        fallback = null
    )
}

@Composable
private fun ChefPicture(pictureUrl: String?){
    StandardImage(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape),
        url = pictureUrl,
        fallback = null
    )
}