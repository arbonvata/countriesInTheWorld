package com.example.countriesoftheworld.data.network

import com.example.countriesoftheworld.data.model.CountryItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CountryApi {
    private val client =
        HttpClient(CIO) {

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    },
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            defaultRequest {
                url("https://www.apicountries.com/")
            }
        }

    suspend fun getCountries(): List<CountryItem> = client.get("countries").body<List<CountryItem>>()

    suspend fun getCountry(countryName: String): List<CountryItem> =
        client
            .get {
                url {
                    // The base URL is already set by defaultRequest
                    // We append path segments here
                    appendPathSegments("name", countryName)
                }
            }.body<List<CountryItem>>()
}
