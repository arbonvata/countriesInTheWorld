package com.example.countriesoftheworld.di

import com.example.countriesoftheworld.data.model.objectbox.CountrySavable
import com.example.countriesoftheworld.data.model.objectbox.ObjectBox
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.objectbox.Box
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient =
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

    @Provides
    @Singleton
    fun provideCountryBox(): Box<CountrySavable> = ObjectBox.store.boxFor(CountrySavable::class.java)
}
