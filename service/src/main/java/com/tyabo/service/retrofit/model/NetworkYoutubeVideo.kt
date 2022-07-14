package com.tyabo.service.retrofit.model

import com.squareup.moshi.Json
import com.tyabo.data.MenuYoutubeVideo

data class NetworkYoutubeVideo(
    val title: String,
    @Json(name = "thumbnail_url") val thumbnailUrl: String
    )