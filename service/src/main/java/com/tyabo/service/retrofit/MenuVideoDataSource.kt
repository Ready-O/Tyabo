package com.tyabo.service.retrofit

import com.tyabo.data.MenuYoutubeVideo

interface MenuVideoDataSource {

    suspend fun importVideo(youtubeUrl: String): Result<MenuYoutubeVideo>
}