package com.example.moviedatabase.data.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviedatabase.data.room_database.dao.FavoriteDao
import com.example.moviedatabase.data.room_database.dao.MovieDao
import com.example.moviedatabase.data.room_database.entities.FavoriteEntity
import com.example.moviedatabase.data.room_database.entities.GenreEntity
import com.example.moviedatabase.data.room_database.entities.MovieDetailsEntity
import com.example.moviedatabase.data.room_database.entities.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
        MovieDetailsEntity::class,
        FavoriteEntity::class,
        GenreEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DATABASE_NAME = "movie_database"
    }
}
