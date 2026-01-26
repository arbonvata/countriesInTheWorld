package com.example.countriesoftheworld.data.repository

import android.util.Log
import com.example.countriesoftheworld.data.network.CountryApiService
import com.example.countriesoftheworld.presentation.compose.Continent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllCountriesRepository
    @Inject
    constructor(
        private val countryApi: CountryApiService,
    ) {
        suspend fun getAllCountries(continent: Continent?) =
            if (continent == null) {
                countryApi.getCountries()
            } else {
                countryApi.getCountries().filter { country ->
                    Log.d(
                        "AllCountriesRepository",
                        "Filtering by continent: ${continent.name}, country region: ${country.region}, subregion: ${country.subregion}",
                    )

                    val continentName = continent.name
                    val region = country.region
                    val subregion = country.subregion

                    when (continentName) {
                        "North America" -> region == "Americas" && subregion != "South America"
                        "South America" -> region == "Americas" && subregion == "South America"
                        "Australia (Oceania)" -> region == "Oceania"
                        "Antarctica" -> region == "Polar" || country.name?.contains("Antarctica", ignoreCase = true) == true
                        else -> region == continentName
                    }
                }
            }

        suspend fun getCountry(name: String) = countryApi.getCountry(name)

        suspend fun getCountriesByLanguage(languageCode: String) = countryApi.getCountriesByLanguage(languageCode)
    }
