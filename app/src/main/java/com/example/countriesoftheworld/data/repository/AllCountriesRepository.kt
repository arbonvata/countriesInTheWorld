package com.example.countriesoftheworld.data.repository

import com.example.countriesoftheworld.data.network.CountryApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllCountriesRepository
    @Inject
    constructor(
        private val countryApi: CountryApiService,
    ) {
        suspend fun getAllCountries() =
            withContext(Dispatchers.IO) {
                countryApi.getCountries()
            }

        suspend fun getCountry(name: String) =
            withContext(Dispatchers.IO) {
                countryApi.getCountry(name)
            }
    }
