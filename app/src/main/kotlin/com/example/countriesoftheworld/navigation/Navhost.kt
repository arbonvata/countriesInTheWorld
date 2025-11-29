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
import com.example.countriesoftheworld.presentation.compose.Continent
import com.example.countriesoftheworld.presentation.compose.ContinentsScreen
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
                navController = navController,
                continent = null, // No continent filter for base route
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
                navController = navController,
                // Pass the continent only if continentName is not null/empty
                continent = if (continentName.isNullOrEmpty()) null else Continent(continentName, "", ""),
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
