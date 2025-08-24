# readme.md

## Project Overview

This is an Android Kotlin app called "Countries of the World" that displays a list of countries with their flags and allows navigation to detailed country information. The app uses Jetpack Compose for UI, Ktor for networking, and follows MVVM architecture pattern.

## Build Commands

**Build the project:**
```bash
./gradlew build
```

**Run lint checks:**
```bash
./gradlew ktlintCheck
./gradlew lint
```

**Auto-fix lint issues:**
```bash
./gradlew ktlintFormat
./gradlew lintFix
```

**Run tests:**
```bash
./gradlew test                    # All unit tests
./gradlew testDebugUnitTest       # Debug unit tests only
./gradlew connectedAndroidTest    # Instrumentation tests (requires device/emulator)
```

**Clean build:**
```bash
./gradlew clean
```

## Architecture & Code Structure

### Layer Architecture
The app follows a standard Android MVVM architecture with clear separation of concerns:

- **Data Layer**: `data/` package containing models, network API client, and repository
- **Presentation Layer**: `presentation/` package with ViewModels and Compose UI screens
- **Navigation**: Centralized navigation setup in `navigation/` package

### Key Architectural Patterns

**Repository Pattern**: `AllCountriesRepository` acts as a single source of truth, wrapping the `CountryApi` and handling data transformations.

**State Management**: Uses StateFlow with sealed interfaces for UI state:
- `AllCountriesUiState` for country list states
- `SingleCountryUiState` for individual country details

**Dependency Creation**: Use hilt for this.
### Data Flow
1. **API Layer**: `CountryApi` uses Ktor client to fetch from `https://www.apicountries.com/`
2. **Repository Layer**: `AllCountriesRepository` handles IO dispatching and error handling
3. **ViewModel Layer**: `CountryViewModel` manages UI state with StateFlow
4. **UI Layer**: Compose screens observe StateFlow and react to state changes

### Navigation Structure
- **All Countries Screen**: Main list showing country names and flags with navigation to details
- **Country Info Screen**: Detail screen showing country name and capital
- Navigation uses string-based routes with country name as parameter

### Data Models
- **CountryItem**: Full API response model with extensive country data (serializable)
- **Country**: Simplified UI model containing only name and flag URL
- Models use Kotlin serialization for JSON parsing

### Key Dependencies
- **UI**: Jetpack Compose with Material3
- **Networking**: Ktor client with CIO engine
- **Image Loading**: Coil3 for async image loading
- **Navigation**: Compose Navigation
- **Code Quality**: KtLint for formatting




**API Response**: The API returns arrays even for single country requests, so `country[0]` is used to get single results.

