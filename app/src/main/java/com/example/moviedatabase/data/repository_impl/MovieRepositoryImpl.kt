package com.example.moviedatabase.data.repository_impl

import com.example.moviedatabase.data.mappers.toEntity
import com.example.moviedatabase.data.mappers.toGenre
import com.example.moviedatabase.data.mappers.toMovie
import com.example.moviedatabase.data.mappers.toMovieDetails
import com.example.moviedatabase.data.room_database.dao.MovieDao
import com.example.moviedatabase.domain.models.Genre
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.models.MovieDetails
import com.example.moviedatabase.domain.network.MovieService
import com.example.moviedatabase.domain.repositories.MovieRepository
import com.example.moviedatabase.utils.Constants
import com.example.moviedatabase.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao,
    private val gson: Gson
) : MovieRepository {

    override suspend fun getPopularMovies(page: Int): Resource<List<Movie>> {
        return getMovies(
            category = "popular",
            page = page,
            apiCall = { movieService.getPopularMovies(Constants.API_KEY, page) }
        )
    }

    override suspend fun getNowPlayingMovies(page: Int): Resource<List<Movie>> {
        return getMovies(
            category = "now_playing",
            page = page,
            apiCall = { movieService.getNowPlayingMovies(Constants.API_KEY, page) }
        )
    }

    override suspend fun getTopRatedMovies(page: Int): Resource<List<Movie>> {
        return getMovies(
            category = "top_rated",
            page = page,
            apiCall = { movieService.getTopRatedMovies(Constants.API_KEY, page) }
        )
    }

    override suspend fun getUpcomingMovies(page: Int): Resource<List<Movie>> {
        return getMovies(
            category = "upcoming",
            page = page,
            apiCall = { movieService.getUpcomingMovies(Constants.API_KEY, page) }
        )
    }

    private suspend fun getMovies(
        category: String,
        page: Int,
        apiCall: suspend () -> com.example.moviedatabase.domain.dto.MovieResponseDto
    ): Resource<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                val movies = response.results.map { it.toMovie() }

                // Cache first page
                if (page == 1) {
                    movieDao.deleteMoviesByCategory(category)
                    movieDao.insertMovies(movies.map { it.toEntity(category) })
                }

                Resource.Success(movies)
            } catch (e: HttpException) {
                val cachedMovies = movieDao.getMoviesByCategory(category).map { it.toMovie() }
                if (cachedMovies.isNotEmpty()) {
                    Resource.Success(cachedMovies)
                } else {
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
                }
            } catch (e: IOException) {
                val cachedMovies = movieDao.getMoviesByCategory(category).map { it.toMovie() }
                if (cachedMovies.isNotEmpty()) {
                    Resource.Success(cachedMovies)
                } else {
                    Resource.Error("Couldn't reach server. Check your internet connection.")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Resource<MovieDetails> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieService.getMovieDetails(movieId, Constants.API_KEY)
                val movieDetails = response.toMovieDetails()

                // Cache the details
                movieDao.insertMovieDetails(movieDetails.toEntity(gson))

                Resource.Success(movieDetails)
            } catch (e: HttpException) {
                val cached = movieDao.getMovieDetails(movieId)
                if (cached != null) {
                    Resource.Success(cached.toMovieDetails(gson))
                } else {
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
                }
            } catch (e: IOException) {
                val cached = movieDao.getMovieDetails(movieId)
                if (cached != null) {
                    Resource.Success(cached.toMovieDetails(gson))
                } else {
                    Resource.Error("Couldn't reach server. Check your internet connection.")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun searchMovies(query: String, page: Int): Resource<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieService.searchMovies(Constants.API_KEY, query, page)
                val movies = response.results.map { it.toMovie() }
                Resource.Success(movies)
            } catch (e: HttpException) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            } catch (e: IOException) {
                Resource.Error("Couldn't reach server. Check your internet connection.")
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun discoverMovies(page: Int, genreIds: String?): Resource<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieService.discoverMovies(
                    apiKey = Constants.API_KEY,
                    page = page,
                    genreIds = genreIds
                )
                val movies = response.results.map { it.toMovie() }
                Resource.Success(movies)
            } catch (e: HttpException) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            } catch (e: IOException) {
                Resource.Error("Couldn't reach server. Check your internet connection.")
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }

    override suspend fun getGenres(): Resource<List<Genre>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = movieService.getGenres(Constants.API_KEY)
                val genres = response.genres.map { it.toGenre() }

                // Cache genres
                movieDao.deleteAllGenres()
                movieDao.insertGenres(genres.map { it.toEntity() })

                Resource.Success(genres)
            } catch (e: HttpException) {
                val cached = movieDao.getAllGenres().map { it.toGenre() }
                if (cached.isNotEmpty()) {
                    Resource.Success(cached)
                } else {
                    Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
                }
            } catch (e: IOException) {
                val cached = movieDao.getAllGenres().map { it.toGenre() }
                if (cached.isNotEmpty()) {
                    Resource.Success(cached)
                } else {
                    Resource.Error("Couldn't reach server. Check your internet connection.")
                }
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}
