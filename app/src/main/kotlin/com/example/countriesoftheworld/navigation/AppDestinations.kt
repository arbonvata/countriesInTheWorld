package com.example.countriesoftheworld.navigation

object AppDestinations {
    const val ALL_COUNTRIES_ARG = "continentName"
    const val ALL_COUNTRIES_BASE_ROUTE = "all_countries"
    const val ALL_COUNTRIES_ROUTE = "all_countries?continentName={$ALL_COUNTRIES_ARG}"

    const val VISITED_COUNTRIES_ROUTE = "visited_countries"
    const val NOT_VISITED_COUNTRIES_ROUTE = "not_visited_countries"

    const val CONTINENTS_ROUTE = "continents"

    const val COUNTRY_INFO_ARG_NAME = "countryName"
    const val COUNTRY_INFO_ROUTE = "countryInfo"
    const val COUNTRY_INFO_ROUTE_WITH_ARG = "$COUNTRY_INFO_ROUTE/{$COUNTRY_INFO_ARG_NAME}"

    const val ALL_LANGUAGES_ROUTE = "all_languages"

    const val COUNTRIES_BY_LANGUAGE_ARG = "languageCode"
    const val COUNTRIES_BY_LANGUAGE_ROUTE = "countries_by_language/{$COUNTRIES_BY_LANGUAGE_ARG}"

    fun allCountriesRoute(continentName: String): String = "all_countries?continentName=$continentName"

    fun countryInfoRoute(countryName: String): String = "$COUNTRY_INFO_ROUTE/$countryName"

    fun countriesByLanguageRoute(languageCode: String): String = "countries_by_language/$languageCode"
}
