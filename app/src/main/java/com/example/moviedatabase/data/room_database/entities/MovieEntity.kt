package com.example.moviedatabase.data.room_database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val genreIds: String, // Stored as comma-separated string
    val adult: Boolean,
    val originalLanguage: String,
    val originalTitle: String,
    val category: String // popular, now_playing, top_rated, upcoming
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val genreIds: String,
    val adult: Boolean,
    val originalLanguage: String,
    val originalTitle: String,
    val addedAt: Long = System.currentTimeMillis()
)
