package com.example.countriesoftheworld.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.countriesoftheworld.presentation.compose.AllCountries
import com.example.countriesoftheworld.presentation.compose.AllLanguagesScreen
import com.example.countriesoftheworld.presentation.compose.Continent
import com.example.countriesoftheworld.presentation.compose.ContinentsScreen
import com.example.countriesoftheworld.presentation.compose.CountriesByLanguageScreen
import com.example.countriesoftheworld.presentation.compose.CountryInfoScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController: NavHostController = rememberNavController() // 1. Create NavController

    NavHost( // 2. Create NavHost
        modifier = modifier,
        navController = navController,
        startDestination = AppDestinations.ALL_COUNTRIES_BASE_ROUTE, // 3. Define the start destination
    ) {
        // 4. Define your composable destinations (screens)
        // Base route for all countries without continent filter
        composable(route = AppDestinations.ALL_COUNTRIES_BASE_ROUTE) {
            AllCountries(
                continent = null, // No continent filter for base route
                navController = navController,
            )
        }

        // Route for all countries with optional continent argument
        composable(
            route = AppDestinations.ALL_COUNTRIES_ROUTE,
            arguments =
                listOf(
                    navArgument(AppDestinations.ALL_COUNTRIES_ARG) {
                        type = NavType.StringType
                        nullable = true // Argument is optional
                    },
                ),
        ) { backStackEntry ->
            // Retrieve the optional continent name
            val continentName = backStackEntry.arguments?.getString(AppDestinations.ALL_COUNTRIES_ARG)
            AllCountries(
                continent = if (continentName.isNullOrEmpty()) null else Continent(continentName, "", ""),
                navController = navController,
            )
        }

        // Route for visited countries
        composable(route = AppDestinations.VISITED_COUNTRIES_ROUTE) {
            AllCountries(
                continent = null,
                visitedFilter = true,
                navController = navController,
            )
        }

        // Route for not visited countries
        composable(route = AppDestinations.NOT_VISITED_COUNTRIES_ROUTE) {
            AllCountries(
                continent = null,
                visitedFilter = false,
                navController = navController,
            )
        }

        composable(route = AppDestinations.CONTINENTS_ROUTE) {
            ContinentsScreen(
                onContinentClick = { continent ->
                    // Navigate to the AllCountries screen with the continent name as an argument
                    navController.navigate(AppDestinations.allCountriesRoute(continent))
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Route for all languages
        composable(route = AppDestinations.ALL_LANGUAGES_ROUTE) {
            AllLanguagesScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanguageClick = { languageCode ->
                    navController.navigate(AppDestinations.countriesByLanguageRoute(languageCode))
                },
            )
        }

        // Route for countries by language
        composable(
            route = AppDestinations.COUNTRIES_BY_LANGUAGE_ROUTE,
            arguments =
                listOf(
                    navArgument(AppDestinations.COUNTRIES_BY_LANGUAGE_ARG) {
                        type = NavType.StringType
                    },
                ),
        ) { backStackEntry ->
            val languageCode = backStackEntry.arguments?.getString(AppDestinations.COUNTRIES_BY_LANGUAGE_ARG) ?: return@composable
            CountriesByLanguageScreen(
                languageCode = languageCode,
                onBackClick = {
                    navController.popBackStack()
                },
                onCountryClick = { countryName ->
                    navController.navigate(AppDestinations.countryInfoRoute(countryName))
                },
                languageName = "",
            )
        }

        composable(
            route = AppDestinations.COUNTRY_INFO_ROUTE_WITH_ARG,
            arguments =
                listOf(
                    navArgument(AppDestinations.COUNTRY_INFO_ARG_NAME) {
                        // Define argument
                        type = NavType.StringType
                        // nullable = false // Default
                        defaultValue = "Unknown" // Optional default value
                    },
                ),
        ) { backStackEntry ->
            // Retrieve the argument
            val countryName = backStackEntry.arguments?.getString(AppDestinations.COUNTRY_INFO_ARG_NAME)
            if (countryName != null) {
                CountryInfoScreen(
                    countryName = countryName,
                    navController = navController,
                )
            } else {
                // Handle error: countryName not found, navigate back
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}
