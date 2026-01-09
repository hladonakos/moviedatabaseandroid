package com.example.moviedatabase.domain.repositories

import com.example.moviedatabase.domain.models.Genre
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.models.MovieDetails
import com.example.moviedatabase.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getPopularMovies(page: Int): Resource<List<Movie>>

    suspend fun getNowPlayingMovies(page: Int): Resource<List<Movie>>

    suspend fun getTopRatedMovies(page: Int): Resource<List<Movie>>

    suspend fun getUpcomingMovies(page: Int): Resource<List<Movie>>

    suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails>

    suspend fun searchMovies(query: String, page: Int): Resource<List<Movie>>

    suspend fun discoverMovies(page: Int, genreIds: String? = null): Resource<List<Movie>>

    suspend fun getGenres(): Resource<List<Genre>>
}
