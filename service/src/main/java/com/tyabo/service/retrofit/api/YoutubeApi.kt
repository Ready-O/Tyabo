package com.tyabo.service.retrofit.api

import com.squareup.moshi.Json
import com.tyabo.service.retrofit.model.NetworkYoutubeVideo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface YoutubeApi {
    @GET("oembed")
    suspend fun importTitleAndThumbnail(@Query("url") url: String): NetworkYoutubeVideo
}