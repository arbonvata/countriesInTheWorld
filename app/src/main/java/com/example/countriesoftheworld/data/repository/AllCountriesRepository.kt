package com.example.countriesoftheworld.data.repository

import com.example.countriesoftheworld.data.network.CountryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Todo make injectable later on
class AllCountriesRepository {
    val api = CountryApi()

    suspend fun getAllCountries() =
        withContext(Dispatchers.IO) {
            api.getCountries()
        }

    suspend fun getCountry(name: String) =
        withContext(Dispatchers.IO) {
            api.getCountry(name)
        }
}
