package com.example.moviedatabase.presentation.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.repositories.MovieRepository
import com.example.moviedatabase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val canLoadMore: Boolean = true,
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update {
                it.copy(
                    movies = emptyList(),
                    hasSearched = false,
                    currentPage = 1,
                    canLoadMore = true
                )
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Debounce
            search()
        }
    }

    private fun search() {
        viewModelScope.launch {
            if (_uiState.value.query.isBlank()) return@launch

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    movies = emptyList(),
                    currentPage = 1,
                    canLoadMore = true,
                    hasSearched = true
                )
            }

            when (val result = movieRepository.searchMovies(_uiState.value.query, 1)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            movies = result.data ?: emptyList(),
                            isLoading = false,
                            currentPage = 2,
                            canLoadMore = (result.data?.size ?: 0) >= 20
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
    }

    fun loadMore() {
        viewModelScope.launch {
            if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) return@launch
            if (_uiState.value.query.isBlank()) return@launch

            _uiState.update { it.copy(isLoadingMore = true) }

            when (val result = movieRepository.searchMovies(
                _uiState.value.query,
                _uiState.value.currentPage
            )) {
                is Resource.Success -> {
                    val newMovies = result.data ?: emptyList()
                    _uiState.update {
                        it.copy(
                            movies = it.movies + newMovies,
                            isLoadingMore = false,
                            currentPage = it.currentPage + 1,
                            canLoadMore = newMovies.size >= 20
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

    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update {
            SearchUiState()
        }
    }
}
