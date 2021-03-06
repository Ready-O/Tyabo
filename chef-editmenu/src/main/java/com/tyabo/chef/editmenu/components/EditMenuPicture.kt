package com.tyabo.chef.editmenu.components

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.tyabo.designsystem.MenuPicture

@Composable
fun EditMenuPicture(
    modifier: Modifier,
    menuPictureUrl: String?,
    menuname: String,
    onClick: (String) -> Unit
) {

    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            onClick(result.uriContent.toString())
        }
    }

    //val confirmButton = stringResource(id = R.string.label_settings_crop_choose)
    MenuPicture(
        modifier = modifier,
        url = menuPictureUrl,
        menuname = menuname,
        onClick = {
            cropImage.launch(
                options {
                    setImageSource(includeGallery = true, includeCamera = false)
                    setOutputCompressFormat(outputCompressFormat = Bitmap.CompressFormat.JPEG)
                    //setCropMenuCropButtonTitle(confirmButton)

                    // We set limited crop size to generate a file that does not exceed 5MB
                    // The Max crop size is approximately 4MB
                    setMinCropResultSize(minCropResultWidth = 40, minCropResultHeight = 40)
                    setMaxCropResultSize(maxCropResultWidth = 2500, maxCropResultHeight = 2500)
                }
            )
        }
    )
}