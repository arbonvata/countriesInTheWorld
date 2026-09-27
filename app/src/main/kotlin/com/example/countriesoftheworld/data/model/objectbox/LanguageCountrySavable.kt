package com.example.countriesoftheworld.data.model.objectbox

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * A language spoken in a country, harvested from API responses so the
 * language list and language lookups can work with real data.
 */
@Entity
data class LanguageCountrySavable(
    @Id var id: Long = 0,
    var languageCode: String? = null,
    var languageName: String? = null,
    var countryCode: String? = null,
    var countryName: String? = null,
)
