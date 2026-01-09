package com.example.moviedatabase.presentation.movie_list_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviedatabase.presentation.generic_compose_views.ErrorMessage
import com.example.moviedatabase.presentation.generic_compose_views.LoadingIndicator
import com.example.moviedatabase.presentation.generic_compose_views.MovieCard
import com.example.moviedatabase.presentation.generic_compose_views.SmallLoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    onMovieClick: (movieId: Int, title: String, posterPath: String?) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = gridState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 6 && !uiState.isLoadingMore && uiState.canLoadMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && uiState.movies.isNotEmpty()) {
            viewModel.loadMoreMovies()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category tabs
        ScrollableTabRow(
            selectedTabIndex = MovieCategory.entries.indexOf(uiState.selectedCategory),
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            MovieCategory.entries.forEach { category ->
                Tab(
                    selected = uiState.selectedCategory == category,
                    onClick = { viewModel.selectCategory(category) },
                    text = {
                        Text(
                            text = when (category) {
                                MovieCategory.POPULAR -> "Popular"
                                MovieCategory.NOW_PLAYING -> "Now Playing"
                                MovieCategory.TOP_RATED -> "Top Rated"
                                MovieCategory.UPCOMING -> "Upcoming"
                            }
                        )
                    }
                )
            }
        }

        // Genre filter chips
        if (uiState.genres.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedGenreId == null,
                        onClick = { viewModel.selectGenre(null) },
                        label = { Text("All") }
                    )
                }
                items(uiState.genres) { genre ->
                    FilterChip(
                        selected = uiState.selectedGenreId == genre.id,
                        onClick = { viewModel.selectGenre(genre.id) },
                        label = { Text(genre.name) }
                    )
                }
            }
        }

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading && uiState.movies.isEmpty() -> {
                    LoadingIndicator()
                }
                uiState.error != null && uiState.movies.isEmpty() -> {
                    ErrorMessage(
                        message = uiState.error ?: "Unknown error",
                        onRetry = { viewModel.retry() }
                    )
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = uiState.isLoading && uiState.movies.isNotEmpty(),
                        onRefresh = { viewModel.loadMovies(refresh = true) }
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 150.dp),
                            state = gridState,
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = uiState.movies,
                                key = { it.id }
                            ) { movie ->
                                MovieCard(
                                    movie = movie,
                                    onClick = {
                                        onMovieClick(movie.id, movie.title, movie.posterPath)
                                    }
                                )
                            }

                            if (uiState.isLoadingMore) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        SmallLoadingIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
