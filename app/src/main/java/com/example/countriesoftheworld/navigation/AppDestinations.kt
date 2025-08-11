package com.example.countriesoftheworld.navigation

object AppDestinations {
    const val ALL_COUNTRIES_ROUTE = "allCountries"
    const val COUNTRY_INFO_ROUTE_BASE = "countryInfo" // Base for routes with arguments
    const val COUNTRY_INFO_ARG_NAME = "countryName"
    val COUNTRY_INFO_ROUTE_WITH_ARG = "$COUNTRY_INFO_ROUTE_BASE/{$COUNTRY_INFO_ARG_NAME}"

    // Helper to create the route for navigation
    fun countryInfoRoute(countryName: String) = "$COUNTRY_INFO_ROUTE_BASE/$countryName"
}
