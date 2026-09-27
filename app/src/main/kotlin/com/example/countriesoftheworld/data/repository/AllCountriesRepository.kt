package com.example.countriesoftheworld.data.repository

import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.data.model.objectbox.CountrySavable
import com.example.countriesoftheworld.data.model.objectbox.LanguageCountrySavable
import com.example.countriesoftheworld.data.model.objectbox.LanguageCountrySavable_
import com.example.countriesoftheworld.data.network.CountryApiService
import com.example.countriesoftheworld.presentation.compose.Continent
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllCountriesRepository
    @Inject
    constructor(
        private val countryApi: CountryApiService,
        private val languageCountryBox: Box<LanguageCountrySavable>,
    ) {
        suspend fun getAllCountries(continent: Continent?) =
            if (continent == null) {
                countryApi.getCountries().also { saveLanguageCountries(it) }
            } else {
                countryApi.getCountries()
                    .also { saveLanguageCountries(it) }
                    .filter { country -> country.matchesContinent(continent) }
            }

        suspend fun getCountry(name: String) = countryApi.getCountry(name)

        suspend fun getCountriesByLanguage(languageCode: String): List<CountryItem> {
            val stored = getCountriesByLanguageFromDb(languageCode)
            if (stored.isNotEmpty()) return stored

            // Language not harvested yet (fresh install, no countries fetch so far)
            // or the API genuinely has no data for it; the endpoint returns an
            // empty list for unknown codes.
            val fetched = countryApi.getCountriesByLanguage(languageCode)
            saveLanguageCountries(fetched)
            return fetched
        }

        /** Countries that speak the given language, from locally harvested data. */
        private fun getCountriesByLanguageFromDb(languageCode: String): List<CountryItem> =
            languageCountryBox
                .query(LanguageCountrySavable_.languageCode.equal(languageCode))
                .build()
                .use { query ->
                    query.find().map { entry ->
                        CountryItem(
                            name = entry.countryName,
                            alpha2Code = entry.countryCode,
                        )
                    }
                }

        /** Remember which languages are spoken where, so language screens can use real data. */
        private suspend fun saveLanguageCountries(countries: List<CountryItem>) {
            val existing =
                languageCountryBox
                    .query()
                    .build()
                    .use { query ->
                        query
                            .find()
                            .mapNotNull { it.key }
                            .toSet()
                    }

            val newEntries =
                countries.flatMap { country ->
                    val countryName = country.name ?: return@flatMap emptyList()
                    country.languages.orEmpty().mapNotNull { language ->
                        val code = language.iso639_1 ?: return@mapNotNull null
                        val key = "$code|$countryName"
                        if (key in existing) {
                            null
                        } else {
                            LanguageCountrySavable(
                                languageCode = code,
                                languageName = language.name,
                                countryCode = country.alpha2Code,
                                countryName = countryName,
                            )
                        }
                    }
                }

            if (newEntries.isNotEmpty()) {
                languageCountryBox.put(newEntries)
            }
        }

        private fun CountryItem.matchesContinent(continent: Continent): Boolean =
            when (continent.name) {
                "North America" -> region == "Americas" && subregion != "South America"
                "South America" -> region == "Americas" && subregion == "South America"
                "Australia (Oceania)" -> region == "Oceania"
                "Antarctica" -> region == "Polar" || name?.contains("Antarctica", ignoreCase = true) == true
                else -> region == continent.name
            }
}

private val LanguageCountrySavable.key: String?
    get() = if (languageCode != null && countryName != null) "$languageCode|$countryName" else null
