package com.tyabo.designsystem.picture

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options

@Composable
fun InputImage(
    inputAction: (String) -> Unit,
    image: @Composable (() -> Unit) -> Unit
){
    val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            inputAction(result.uriContent.toString())
        }
    }

    image {
        //val confirmButton = stringResource(id = R.string.label_settings_crop_choose)
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
}