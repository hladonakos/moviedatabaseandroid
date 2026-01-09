package com.example.moviedatabase.presentation.movie_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.models.MovieDetails
import com.example.moviedatabase.domain.repositories.FavoriteRepository
import com.example.moviedatabase.domain.repositories.MovieRepository
import com.example.moviedatabase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailsUiState(
    val movieDetails: MovieDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private var currentMovieId: Int = -1

    fun loadMovieDetails(movieId: Int) {
        if (movieId == currentMovieId && _uiState.value.movieDetails != null) return
        currentMovieId = movieId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = movieRepository.getMovieDetails(movieId)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            movieDetails = result.data,
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }

        // Observe favorite status
        viewModelScope.launch {
            favoriteRepository.isFavoriteFlow(movieId).collect { isFavorite ->
                _uiState.update { it.copy(isFavorite = isFavorite) }
            }
        }
    }

    fun toggleFavorite() {
        val movieDetails = _uiState.value.movieDetails ?: return

        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                favoriteRepository.removeFromFavorites(movieDetails.id)
            } else {
                val movie = Movie(
                    id = movieDetails.id,
                    title = movieDetails.title,
                    overview = movieDetails.overview,
                    posterPath = movieDetails.posterPath,
                    backdropPath = movieDetails.backdropPath,
                    releaseDate = movieDetails.releaseDate,
                    voteAverage = movieDetails.voteAverage,
                    voteCount = movieDetails.voteCount,
                    popularity = movieDetails.popularity,
                    genreIds = movieDetails.genres.map { it.id },
                    adult = movieDetails.adult,
                    originalLanguage = movieDetails.originalLanguage,
                    originalTitle = movieDetails.originalTitle
                )
                favoriteRepository.addToFavorites(movie)
            }
        }
    }

    fun retry() {
        if (currentMovieId != -1) {
            loadMovieDetails(currentMovieId)
        }
    }
}
