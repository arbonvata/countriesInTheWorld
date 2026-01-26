package com.example.countriesoftheworld.di

import com.example.countriesoftheworld.data.repository.LanguageRepository
import com.example.countriesoftheworld.data.repository.LanguageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLanguageRepository(languageRepositoryImpl: LanguageRepositoryImpl): LanguageRepository
}
