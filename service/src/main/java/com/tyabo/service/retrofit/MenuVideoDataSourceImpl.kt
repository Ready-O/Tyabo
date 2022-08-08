package com.tyabo.service.retrofit

import com.tyabo.data.MenuYoutubeVideo
import com.tyabo.service.retrofit.api.YoutubeApi
import javax.inject.Inject

class MenuVideoDataSourceImpl @Inject constructor(
    private val youtubeApi: YoutubeApi
) : MenuVideoDataSource {

    override suspend fun importVideo(youtubeUrl: String): Result<MenuYoutubeVideo> {
        val url = extractVideoUrl(youtubeUrl)
        return if (url == null) {
            Result.failure(Exception())
        } else {
            return try {
                val networkInfo = youtubeApi.importTitleAndThumbnail(url)
                Result.success(
                    MenuYoutubeVideo(
                    title = networkInfo.title,
                    thumbnailUrl = networkInfo.thumbnailUrl,
                    videoUrl = url
                    )
                )
            }
            catch (e: Exception){
                Result.failure(e)
            }
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