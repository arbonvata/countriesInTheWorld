package com.example.countriesoftheworld.data.repository

import android.content.Context
import com.example.countriesoftheworld.data.model.LanguageData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

interface LanguageRepository {
    suspend fun getLanguages(): List<LanguageData>
}

@Singleton
class LanguageRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val json: Json,
    ) : LanguageRepository {
        override suspend fun getLanguages(): List<LanguageData> =
            withContext(Dispatchers.IO) {
                try {
                    val jsonString =
                        context.assets
                            .open("languages.json")
                            .bufferedReader()
                            .use { it.readText() }
                    json.decodeFromString<List<LanguageData>>(jsonString)
                } catch (e: Exception) {
                    emptyList()
                }
            }
    }
