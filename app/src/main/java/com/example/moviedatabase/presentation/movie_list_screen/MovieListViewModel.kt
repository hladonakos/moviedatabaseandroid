package com.example.moviedatabase.presentation.movie_list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedatabase.domain.models.Genre
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.repositories.MovieRepository
import com.example.moviedatabase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class MovieCategory {
    POPULAR, NOW_PLAYING, TOP_RATED, UPCOMING
}

data class MovieListUiState(
    val movies: List<Movie> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val selectedCategory: MovieCategory = MovieCategory.POPULAR,
    val selectedGenreId: Int? = null,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true
)

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieListUiState())
    val uiState: StateFlow<MovieListUiState> = _uiState.asStateFlow()

    init {
        loadGenres()
        loadMovies()
    }

    private fun loadGenres() {
        viewModelScope.launch {
            when (val result = movieRepository.getGenres()) {
                is Resource.Success -> {
                    _uiState.update { it.copy(genres = result.data ?: emptyList()) }
                }
                is Resource.Error -> {
                    // Genres are optional, don't show error
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun loadMovies(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _uiState.update { it.copy(currentPage = 1, canLoadMore = true) }
            }

            if (_uiState.value.isLoading) return@launch

            _uiState.update { it.copy(isLoading = refresh || it.movies.isEmpty(), error = null) }

            val page = if (refresh) 1 else _uiState.value.currentPage

            val result = if (_uiState.value.selectedGenreId != null) {
                movieRepository.discoverMovies(
                    page = page,
                    genreIds = _uiState.value.selectedGenreId.toString()
                )
            } else {
                when (_uiState.value.selectedCategory) {
                    MovieCategory.POPULAR -> movieRepository.getPopularMovies(page)
                    MovieCategory.NOW_PLAYING -> movieRepository.getNowPlayingMovies(page)
                    MovieCategory.TOP_RATED -> movieRepository.getTopRatedMovies(page)
                    MovieCategory.UPCOMING -> movieRepository.getUpcomingMovies(page)
                }
            }

            when (result) {
                is Resource.Success -> {
                    val newMovies = result.data ?: emptyList()
                    _uiState.update { state ->
                        state.copy(
                            movies = if (refresh || page == 1) newMovies else state.movies + newMovies,
                            isLoading = false,
                            currentPage = page + 1,
                            canLoadMore = newMovies.isNotEmpty()
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun loadMoreMovies() {
        viewModelScope.launch {
            if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) return@launch

            _uiState.update { it.copy(isLoadingMore = true) }

            val result = if (_uiState.value.selectedGenreId != null) {
                movieRepository.discoverMovies(
                    page = _uiState.value.currentPage,
                    genreIds = _uiState.value.selectedGenreId.toString()
                )
            } else {
                when (_uiState.value.selectedCategory) {
                    MovieCategory.POPULAR -> movieRepository.getPopularMovies(_uiState.value.currentPage)
                    MovieCategory.NOW_PLAYING -> movieRepository.getNowPlayingMovies(_uiState.value.currentPage)
                    MovieCategory.TOP_RATED -> movieRepository.getTopRatedMovies(_uiState.value.currentPage)
                    MovieCategory.UPCOMING -> movieRepository.getUpcomingMovies(_uiState.value.currentPage)
                }
            }

            when (result) {
                is Resource.Success -> {
                    val newMovies = result.data ?: emptyList()
                    _uiState.update { state ->
                        state.copy(
                            movies = state.movies + newMovies,
                            isLoadingMore = false,
                            currentPage = state.currentPage + 1,
                            canLoadMore = newMovies.isNotEmpty()
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoadingMore = false) }
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun selectCategory(category: MovieCategory) {
        if (_uiState.value.selectedCategory != category) {
            _uiState.update {
                it.copy(
                    selectedCategory = category,
                    selectedGenreId = null,
                    movies = emptyList(),
                    currentPage = 1,
                    canLoadMore = true
                )
            }
            loadMovies()
        }
    }

    fun selectGenre(genreId: Int?) {
        if (_uiState.value.selectedGenreId != genreId) {
            _uiState.update {
                it.copy(
                    selectedGenreId = genreId,
                    movies = emptyList(),
                    currentPage = 1,
                    canLoadMore = true
                )
            }
            loadMovies()
        }
    }

    fun retry() {
        loadMovies(refresh = true)
    }
}
