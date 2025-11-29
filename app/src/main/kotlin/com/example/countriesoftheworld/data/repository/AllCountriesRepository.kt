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
                countryApi.getCountries().filter {
                    Log.d("AllCountriesRepository", "Filtering by continent: ${continent.name} and region: ${it.region}")
                    it.region == continent.name
                }
            }

        suspend fun getCountry(name: String) = countryApi.getCountry(name)
    }
