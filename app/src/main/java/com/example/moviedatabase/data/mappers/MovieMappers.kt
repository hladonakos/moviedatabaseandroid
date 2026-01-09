package com.example.moviedatabase.data.mappers

import com.example.moviedatabase.data.room_database.entities.FavoriteEntity
import com.example.moviedatabase.data.room_database.entities.GenreEntity
import com.example.moviedatabase.data.room_database.entities.MovieDetailsEntity
import com.example.moviedatabase.data.room_database.entities.MovieEntity
import com.example.moviedatabase.domain.dto.GenreDto
import com.example.moviedatabase.domain.dto.MovieDetailsDto
import com.example.moviedatabase.domain.dto.MovieDto
import com.example.moviedatabase.domain.dto.ProductionCompanyDto
import com.example.moviedatabase.domain.dto.ProductionCountryDto
import com.example.moviedatabase.domain.dto.SpokenLanguageDto
import com.example.moviedatabase.domain.models.Genre
import com.example.moviedatabase.domain.models.Movie
import com.example.moviedatabase.domain.models.MovieDetails
import com.example.moviedatabase.domain.models.ProductionCompany
import com.example.moviedatabase.domain.models.ProductionCountry
import com.example.moviedatabase.domain.models.SpokenLanguage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// DTO to Domain
fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genreIds = genreIds,
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}

fun MovieDetailsDto.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        status = status,
        tagline = tagline,
        genres = genres.map { it.toGenre() },
        productionCompanies = productionCompanies.map { it.toProductionCompany() },
        productionCountries = productionCountries.map { it.toProductionCountry() },
        spokenLanguages = spokenLanguages.map { it.toSpokenLanguage() },
        homepage = homepage
    )
}

fun GenreDto.toGenre(): Genre {
    return Genre(id = id, name = name)
}

fun ProductionCompanyDto.toProductionCompany(): ProductionCompany {
    return ProductionCompany(
        id = id,
        name = name,
        logoPath = logoPath,
        originCountry = originCountry
    )
}

fun ProductionCountryDto.toProductionCountry(): ProductionCountry {
    return ProductionCountry(iso = iso, name = name)
}

fun SpokenLanguageDto.toSpokenLanguage(): SpokenLanguage {
    return SpokenLanguage(iso = iso, name = name, englishName = englishName)
}

// Entity to Domain
fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genreIds = genreIds.split(",").filter { it.isNotEmpty() }.map { it.toInt() },
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}

fun FavoriteEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genreIds = genreIds.split(",").filter { it.isNotEmpty() }.map { it.toInt() },
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}

fun MovieDetailsEntity.toMovieDetails(gson: Gson): MovieDetails {
    val genreListType = object : TypeToken<List<Genre>>() {}.type
    val companyListType = object : TypeToken<List<ProductionCompany>>() {}.type
    val countryListType = object : TypeToken<List<ProductionCountry>>() {}.type
    val languageListType = object : TypeToken<List<SpokenLanguage>>() {}.type

    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        status = status,
        tagline = tagline,
        genres = gson.fromJson(genres, genreListType) ?: emptyList(),
        productionCompanies = gson.fromJson(productionCompanies, companyListType) ?: emptyList(),
        productionCountries = gson.fromJson(productionCountries, countryListType) ?: emptyList(),
        spokenLanguages = gson.fromJson(spokenLanguages, languageListType) ?: emptyList(),
        homepage = homepage
    )
}

fun GenreEntity.toGenre(): Genre {
    return Genre(id = id, name = name)
}

// Domain to Entity
fun Movie.toEntity(category: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genreIds = genreIds.joinToString(","),
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        category = category
    )
}

fun Movie.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        genreIds = genreIds.joinToString(","),
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle
    )
}

fun MovieDetails.toEntity(gson: Gson): MovieDetailsEntity {
    return MovieDetailsEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        status = status,
        tagline = tagline,
        genres = gson.toJson(genres),
        productionCompanies = gson.toJson(productionCompanies),
        productionCountries = gson.toJson(productionCountries),
        spokenLanguages = gson.toJson(spokenLanguages),
        homepage = homepage
    )
}

fun Genre.toEntity(): GenreEntity {
    return GenreEntity(id = id, name = name)
}
