package com.example.countriesoftheworld.data.network

import com.example.countriesoftheworld.data.model.CountryItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import javax.inject.Inject

interface CountryApiService {
    suspend fun getCountries(): List<CountryItem>

    suspend fun getCountry(countryName: String): List<CountryItem>
}

class CountryApi
    @Inject
    constructor(
        private val client: HttpClient,
    ) : CountryApiService {
        override suspend fun getCountries(): List<CountryItem> = client.get("countries").body<List<CountryItem>>()

        override suspend fun getCountry(countryName: String): List<CountryItem> =
            client
                .get {
                    url {
                        // The base URL is already set by defaultRequest
                        // We append path segments here
                        appendPathSegments("name", countryName)
                    }
                }.body<List<CountryItem>>()
    }
