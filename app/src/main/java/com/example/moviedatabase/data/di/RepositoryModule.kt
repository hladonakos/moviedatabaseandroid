package com.example.moviedatabase.data.di

import com.example.moviedatabase.data.repository_impl.FavoriteRepositoryImpl
import com.example.moviedatabase.data.repository_impl.MovieRepositoryImpl
import com.example.moviedatabase.domain.repositories.FavoriteRepository
import com.example.moviedatabase.domain.repositories.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}
