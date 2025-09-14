package com.example.countriesoftheworld.data.repository

import com.example.countriesoftheworld.data.network.CountryApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllCountriesRepository
    @Inject
    constructor(
        private val countryApi: CountryApiService,
    ) {
        suspend fun getAllCountries() = countryApi.getCountries()

        suspend fun getCountry(name: String) = countryApi.getCountry(name)
    }
