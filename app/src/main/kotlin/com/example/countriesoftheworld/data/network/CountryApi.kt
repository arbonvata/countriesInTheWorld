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

    suspend fun getCountriesByLanguage(languageCode: String): List<CountryItem>
}

class CountryApi
    @Inject
    constructor(
        private val client: HttpClient,
    ) : CountryApiService {
        override suspend fun getCountries(): List<CountryItem> = client.get("countries").body()

        override suspend fun getCountry(countryName: String): List<CountryItem> =
            client
                .get {
                    url {
                        appendPathSegments("name", countryName)
                    }
                }.body()

        override suspend fun getCountriesByLanguage(languageCode: String): List<CountryItem> =
            client
                .get {
                    url {
                        appendPathSegments("lang", languageCode)
                    }
                }.body()
    }
