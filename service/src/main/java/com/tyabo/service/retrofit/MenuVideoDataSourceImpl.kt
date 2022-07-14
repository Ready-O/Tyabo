package com.tyabo.service.retrofit

import com.tyabo.data.MenuYoutubeVideo
import com.tyabo.service.retrofit.api.YoutubeApi
import javax.inject.Inject

class MenuVideoDataSourceImpl @Inject constructor(
    private val youtubeApi: YoutubeApi
) : MenuVideoDataSource {

    override suspend fun importVideo(youtubeUrl: String): MenuYoutubeVideo? {
        val url = extractVideoUrl(youtubeUrl)
        return if (url == null) {
            null
        } else {
            val networkInfo = youtubeApi.importTitleAndThumbnail(url)
            return MenuYoutubeVideo(
                title = networkInfo.title,
                thumbnailUrl = networkInfo.thumbnailUrl,
                videoUrl = url
            )
        }
    }

    /*
        Result can of form
        - "youtube.com/watch?v=haMOUb3KVSo"
        - "https://youtube.com/watch?v=haMOUb3KVSo"
        - "https://youtu.be/haMOUb3KVSo"
     */
    private fun extractVideoUrl(youtubeUrl: String): String? {
            return youtubeUrl
    }
}