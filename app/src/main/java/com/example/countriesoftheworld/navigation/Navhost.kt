package com.example.countriesoftheworld.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.countriesoftheworld.presentation.compose.AllCountries
import com.example.countriesoftheworld.presentation.compose.CountryInfoScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController() // 1. Create NavController

    NavHost( // 2. Create NavHost
        navController = navController,
        startDestination = AppDestinations.ALL_COUNTRIES_ROUTE, // 3. Define the start destination
    ) {
        // 4. Define your composable destinations (screens)
        composable(route = AppDestinations.ALL_COUNTRIES_ROUTE) {
            // This 'it' is a NavBackStackEntry, not used directly here often
            AllCountries(navController = navController)
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
                CountryInfoScreen(countryName = countryName, navController = navController)
            } else {
                // Handle error: countryName not found, perhaps navigate back or show an error
                Text("Error: Country name not provided.")
                // Example: navController.popBackStack()
            }
        }

        // You can add more destinations here
        // composable(route = "anotherScreen") { AnotherScreen(navController) }
    }
}
