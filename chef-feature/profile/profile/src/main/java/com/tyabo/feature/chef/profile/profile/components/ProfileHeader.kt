package com.tyabo.feature.chef.profile.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tyabo.designsystem.components.buttons.TextButton
import com.tyabo.designsystem.picture.StandardImage

@Composable
fun ProfileHeader(
    name: String,
    chefPictureUrl: String?,
    businessPictureUrl: String?,
    bio: String,
    editProfile: () -> Unit
){
  Column {
      BusinessPicture(pictureUrl = businessPictureUrl)
      Row(modifier = Modifier.padding(16.dp)){
          ChefPicture(chefPictureUrl)
          Text(
              modifier = Modifier.padding(start = 8.dp),
              text = name
          )
      }
      if (bio.isNotEmpty()){
          Text(
              modifier = Modifier.padding(16.dp),
              text = bio
          )
      }
      TextButton(leadingIcon = null, onClick = editProfile) {
          Text("Edit profile")
      }
  }
}

@Composable
private fun BusinessPicture(pictureUrl: String?){
    StandardImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        url = pictureUrl,
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