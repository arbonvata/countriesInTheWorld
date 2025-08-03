package com.example.countriesoftheworld.data.network

import com.example.countriesoftheworld.data.model.Countries
import com.example.countriesoftheworld.data.model.Country
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

    suspend fun getCountries(): Countries = client.get("countries").body<Countries>()

    suspend fun getCountry(name: String): Country = client.get(name).body<Country>()
}
