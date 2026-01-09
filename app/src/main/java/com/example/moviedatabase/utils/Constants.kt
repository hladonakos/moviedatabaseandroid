package com.example.moviedatabase.utils

import com.example.moviedatabase.BuildConfig

object Constants {
    const val BASE_URL = BuildConfig.TMDB_BASE_URL
    const val IMAGE_BASE_URL = BuildConfig.TMDB_IMAGE_BASE_URL
    const val API_KEY = BuildConfig.TMDB_API_KEY

    const val POSTER_SIZE_W185 = "w185"
    const val POSTER_SIZE_W342 = "w342"
    const val POSTER_SIZE_W500 = "w500"
    const val POSTER_SIZE_ORIGINAL = "original"

    const val BACKDROP_SIZE_W300 = "w300"
    const val BACKDROP_SIZE_W780 = "w780"
    const val BACKDROP_SIZE_W1280 = "w1280"
    const val BACKDROP_SIZE_ORIGINAL = "original"

    fun getPosterUrl(path: String?, size: String = POSTER_SIZE_W342): String? {
        return path?.let { "$IMAGE_BASE_URL$size$it" }
    }

    fun getBackdropUrl(path: String?, size: String = BACKDROP_SIZE_W780): String? {
        return path?.let { "$IMAGE_BASE_URL$size$it" }
    }
}
