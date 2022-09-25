package com.tyabo.chef.editmenu

sealed interface YoutubeVideoState {

    object Loading: YoutubeVideoState

    object Empty: YoutubeVideoState

    data class Video(
        val title: String,
        val thumbnailUrl: String,
        val videoUrl: String
    ): YoutubeVideoState
}