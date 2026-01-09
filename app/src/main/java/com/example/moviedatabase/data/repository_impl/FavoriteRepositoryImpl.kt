package com.example.moviedatabase.data.repository_impl

import com.example.moviedatabase.data.mappers.toFavoriteEntity
import com.example.moviedatabase.data.mappers.toMovie
import com.example.moviedatabase.data.room_database.dao.FavoriteDao
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.repositories.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<Movie>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toMovie() }
        }
    }

    override suspend fun addToFavorites(movie: Movie) {
        favoriteDao.insertFavorite(movie.toFavoriteEntity())
    }

    override suspend fun removeFromFavorites(movieId: Int) {
        favoriteDao.deleteFavorite(movieId)
    }

    override suspend fun isFavorite(movieId: Int): Boolean {
        return favoriteDao.isFavorite(movieId)
    }

    override fun isFavoriteFlow(movieId: Int): Flow<Boolean> {
        return favoriteDao.isFavoriteFlow(movieId)
    }
}
