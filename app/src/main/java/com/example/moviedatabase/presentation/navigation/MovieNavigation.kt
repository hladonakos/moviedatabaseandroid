package com.example.moviedatabase.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.moviedatabase.presentation.favorites_screen.FavoritesScreen
import com.example.moviedatabase.presentation.movie_details_screen.MovieDetailsScreen
import com.example.moviedatabase.presentation.movie_list_screen.MovieListScreen
import com.example.moviedatabase.presentation.search_screen.SearchScreen

@Composable
fun MovieNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MovieList,
        modifier = Modifier.padding(paddingValues),
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        composable<Screen.MovieList> {
            MovieListScreen(
                onMovieClick = { movieId, title, posterPath ->
                    navController.navigate(
                        Screen.MovieDetails(
                            movieId = movieId,
                            movieTitle = title,
                            posterPath = posterPath
                        )
                    )
                }
            )
        }

        composable<Screen.MovieDetails> { backStackEntry ->
            val details = backStackEntry.toRoute<Screen.MovieDetails>()
            MovieDetailsScreen(
                movieId = details.movieId,
                movieTitle = details.movieTitle,
                posterPath = details.posterPath,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<Screen.Search> {
            SearchScreen(
                onMovieClick = { movieId, title, posterPath ->
                    navController.navigate(
                        Screen.MovieDetails(
                            movieId = movieId,
                            movieTitle = title,
                            posterPath = posterPath
                        )
                    )
                }
            )
        }

        composable<Screen.Favorites> {
            FavoritesScreen(
                onMovieClick = { movieId, title, posterPath ->
                    navController.navigate(
                        Screen.MovieDetails(
                            movieId = movieId,
                            movieTitle = title,
                            posterPath = posterPath
                        )
                    )
                }
            )
        }
    }
}
