# Movie Database

A modern Android application for browsing, searching, and managing your favorite movies. Built with Jetpack Compose and following Clean Architecture principles.

![Android](https://img.shields.io/badge/Android-29+-green.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.12.01-blue.svg)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-orange.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

## Features

- **Browse Movies** - Explore movies by category: Popular, Now Playing, Top Rated, and Upcoming
- **Search** - Real-time search with debounced input for smooth performance
- **Movie Details** - View comprehensive information including cast, production details, and financials
- **Favorites** - Bookmark movies for quick access later
- **Genre Filtering** - Filter movies by genre for targeted discovery
- **Offline Support** - Local caching with automatic fallback when offline
- **Pagination** - Infinite scrolling for seamless browsing
- **Material Design 3** - Modern UI with dynamic theming and dark/light mode support

## Screenshots

<!-- Add your screenshots here -->
<!-- ![Home Screen](screenshots/home.png) -->
<!-- ![Movie Details](screenshots/details.png) -->
<!-- ![Search](screenshots/search.png) -->

## Architecture

This project follows **Clean Architecture** with clear separation into three layers:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Screens   │  │  ViewModels │  │  Navigation/Routes  │  │
│  │  (Compose)  │  │ (StateFlow) │  │   (Type-safe)       │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Models    │  │    DTOs     │  │ Repository Contracts│  │
│  │  (Kotlin)   │  │  (Response) │  │    (Interfaces)     │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       DATA LAYER                             │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   Remote    │  │    Local    │  │    Repositories     │  │
│  │ (Retrofit)  │  │   (Room)    │  │  (Implementation)   │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Design Patterns

| Pattern | Implementation |
|---------|----------------|
| **MVVM** | ViewModels with StateFlow for reactive UI state |
| **Repository** | Abstract data sources behind interfaces |
| **Dependency Injection** | Hilt for compile-time DI |
| **Mapper** | DTO ↔ Domain Model ↔ Entity conversions |
| **Sealed Classes** | Type-safe navigation routes and UI states |
| **Offline-First** | Cache-first with network fallback |

## Tech Stack

### Core
| Technology | Version | Purpose |
|------------|---------|---------|
| Kotlin | 2.0.0 | Programming language |
| Android SDK | 29 - 35 | Target platforms |
| Jetpack Compose | BOM 2024.12.01 | Declarative UI framework |
| Material3 | Latest | Design system |

### Architecture Components
| Library | Version | Purpose |
|---------|---------|---------|
| Hilt | 2.52 | Dependency injection |
| Navigation Compose | 2.8.5 | Type-safe navigation |
| Lifecycle ViewModel | Latest | UI state management |
| Room | 2.6.1 | Local database |

### Networking
| Library | Version | Purpose |
|---------|---------|---------|
| Retrofit | 2.11.0 | REST API client |
| OkHttp | 4.12.0 | HTTP client & logging |
| Gson | 2.11.0 | JSON serialization |

### Async & Reactive
| Library | Version | Purpose |
|---------|---------|---------|
| Kotlin Coroutines | 1.9.0 | Asynchronous programming |
| StateFlow | - | Reactive state management |

### UI & Media
| Library | Version | Purpose |
|---------|---------|---------|
| Coil | 2.7.0 | Image loading |
| Palette | 1.0.0 | Color extraction |
| Kotlin Serialization | 1.7.3 | Type-safe route arguments |

### Build Tools
| Tool | Version |
|------|---------|
| Android Gradle Plugin | 8.8.0 |
| KSP | 2.0.0-1.0.24 |
| Gradle Version Catalog | libs.versions.toml |

## Project Structure

```
app/src/main/java/com/example/moviedatabase/
├── data/
│   ├── di/                     # Hilt modules
│   │   ├── DatabaseModule.kt
│   │   ├── NetworkModule.kt
│   │   └── RepositoryModule.kt
│   ├── local/                  # Room database
│   │   ├── MovieDatabase.kt
│   │   ├── dao/
│   │   └── entity/
│   ├── mapper/                 # Data transformations
│   └── repository/             # Repository implementations
│
├── domain/
│   ├── model/                  # Business models
│   │   ├── Movie.kt
│   │   └── MovieDetails.kt
│   ├── dto/                    # API response models
│   ├── repository/             # Repository interfaces
│   └── service/                # API service definitions
│
├── presentation/
│   ├── navigation/             # Navigation setup
│   │   ├── Screen.kt
│   │   ├── MovieNavigation.kt
│   │   └── BottomNavItem.kt
│   ├── screens/                # UI screens
│   │   ├── movielist/
│   │   ├── moviedetails/
│   │   ├── search/
│   │   └── favorites/
│   └── generic_compose_views/  # Reusable components
│
├── ui/theme/                   # Material3 theming
├── utils/                      # Utility classes
├── MainActivity.kt
└── MovieApplication.kt
```

## Database Schema

```
┌─────────────────┐     ┌─────────────────────┐
│     movies      │     │    movie_details    │
├─────────────────┤     ├─────────────────────┤
│ id (PK)         │     │ id (PK)             │
│ title           │     │ title               │
│ overview        │     │ overview            │
│ posterPath      │     │ posterPath          │
│ backdropPath    │     │ backdropPath        │
│ releaseDate     │     │ releaseDate         │
│ voteAverage     │     │ voteAverage         │
│ voteCount       │     │ runtime             │
│ genreIds        │     │ budget              │
│ category        │     │ revenue             │
└─────────────────┘     │ genres              │
                        │ productionCompanies │
┌─────────────────┐     └─────────────────────┘
│    favorites    │
├─────────────────┤     ┌─────────────────┐
│ id (PK)         │     │     genres      │
│ title           │     ├─────────────────┤
│ posterPath      │     │ id (PK)         │
│ voteAverage     │     │ name            │
│ addedAt         │     └─────────────────┘
└─────────────────┘
```

## API Integration

This app uses [The Movie Database (TMDB) API](https://www.themoviedb.org/documentation/api) for movie data.

### Endpoints Used

| Endpoint | Description |
|----------|-------------|
| `GET /movie/popular` | Popular movies |
| `GET /movie/now_playing` | Currently in theaters |
| `GET /movie/top_rated` | Highest rated movies |
| `GET /movie/upcoming` | Coming soon |
| `GET /movie/{id}` | Movie details |
| `GET /search/movie` | Search by query |
| `GET /discover/movie` | Filter by genre |
| `GET /genre/movie/list` | Genre catalog |

## Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2.1) or later
- JDK 17
- Android SDK 35
- TMDB API Key ([Get one here](https://www.themoviedb.org/settings/api))

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/moviedatabase.git
   cd moviedatabase
   ```

2. **Add your TMDB API key**

   Open `app/build.gradle.kts` and replace the API key:
   ```kotlin
   buildConfigField("String", "TMDB_API_KEY", "\"your_api_key_here\"")
   ```

3. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

   Or open the project in Android Studio and click Run.

### Configuration

The app configuration can be found in `app/build.gradle.kts`:

```kotlin
android {
    namespace = "com.example.moviedatabase"
    compileSdk = 35

    defaultConfig {
        minSdk = 29
        targetSdk = 35
    }
}
```

## Data Flow

```
User Interaction
       │
       ▼
┌──────────────┐
│   Screen     │  Composable UI
└──────────────┘
       │ collect state
       ▼
┌──────────────┐
│  ViewModel   │  Holds UI State (StateFlow)
└──────────────┘
       │ call repository
       ▼
┌──────────────┐
│  Repository  │  Data orchestration
└──────────────┘
       │
   ┌───┴───┐
   ▼       ▼
┌──────┐ ┌──────┐
│Remote│ │Local │
│ API  │ │ Room │
└──────┘ └──────┘
```

### Example: Loading Popular Movies

1. `MovieListScreen` collects state from `MovieListViewModel`
2. ViewModel calls `movieRepository.getPopularMovies(page)`
3. Repository fetches from TMDB API via Retrofit
4. On success: Maps DTO → Domain Model, caches to Room
5. On error: Returns cached data from Room
6. ViewModel updates `StateFlow<MovieListUiState>`
7. Compose automatically recomposes with new state



## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [The Movie Database (TMDB)](https://www.themoviedb.org/) for the comprehensive movie API
- [Android Developers](https://developer.android.com/) for excellent documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose) team for the modern UI toolkit

---

<p align="center">
  Made with Kotlin and Jetpack Compose
</p>
