package com.example.countriesoftheworld.data.repository

import android.content.Context
import com.example.countriesoftheworld.data.model.LanguageData
import com.example.countriesoftheworld.data.model.objectbox.LanguageCountrySavable
import com.example.countriesoftheworld.data.model.objectbox.LanguageCountrySavable_
import dagger.hilt.android.qualifiers.ApplicationContext
import io.objectbox.Box
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
        private val languageCountryBox: Box<LanguageCountrySavable>,
    ) : LanguageRepository {
        override suspend fun getLanguages(): List<LanguageData> =
            withContext(Dispatchers.IO) {
                val bundled = loadBundledLanguages().associateBy { it.code }

                // Union of the bundled list (all ISO 639-1 codes, incl. ones the
                // API has no data for, like Akan) and every language actually
                // reported by the API (which carries the API's language names).
                languageCountryBox
                    .query()
                    .build()
                    .use { query ->
                        query.find().mapNotNull { it.languageCode }.toSet()
                    }.let { harvestedCodes ->
                        (
                            bundled.values +
                                harvestedCodes
                                    .filter { it !in bundled }
                                    .map { code ->
                                        // API-only language, no bundled entry
                                        val stored =
                                            languageCountryBox
                                                .query(
                                                    LanguageCountrySavable_.languageCode.equal(code),
                                                ).build()
                                                .use { q -> q.findFirst() }
                                        LanguageData(
                                            code = code,
                                            name = stored?.languageName ?: code,
                                            native = stored?.languageName ?: code,
                                        )
                                    }
                        ).sortedBy { it.name }
                    }
            }

        private fun loadBundledLanguages(): List<LanguageData> =
            try {
                context.assets
                    .open("languages.json")
                    .bufferedReader()
                    .use { it.readText() }
                    .let { json.decodeFromString<List<LanguageData>>(it) }
            } catch (e: Exception) {
                emptyList()
            }
    }
