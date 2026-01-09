package com.example.moviedatabase.domain.repositories

import com.example.moviedatabase.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getAllFavorites(): Flow<List<Movie>>

    suspend fun addToFavorites(movie: Movie)

    suspend fun removeFromFavorites(movieId: Int)

    suspend fun isFavorite(movieId: Int): Boolean

    fun isFavoriteFlow(movieId: Int): Flow<Boolean>
}
