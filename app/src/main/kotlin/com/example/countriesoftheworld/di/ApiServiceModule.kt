package com.example.countriesoftheworld.di

import com.example.countriesoftheworld.data.network.CountryApi
import com.example.countriesoftheworld.data.network.CountryApiService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiServiceModule {
    @Binds
    @Singleton
    abstract fun bindCountryApiService(countryApi: CountryApi): CountryApiService
}
