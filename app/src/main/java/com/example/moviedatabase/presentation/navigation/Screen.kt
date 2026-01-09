package com.example.moviedatabase.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object MovieList : Screen

    @Serializable
    data class MovieDetails(
        val movieId: Int,
        val movieTitle: String,
        val posterPath: String?
    ) : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Favorites : Screen
}
