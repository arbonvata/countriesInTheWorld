package com.example.countriesoftheworld.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// flagcdn.com serves a flag for every ISO 3166-1 alpha-2 code (verified for all
// codes returned by the API), unlike the Wikimedia thumbnails the API returns,
// which 403 for some files (e.g. Afghanistan's).
fun CountryItem.flagImageUrl(): String =
    alpha2Code
        ?.lowercase()
        ?.takeIf { it.length == 2 && it.all(Char::isLetter) }
        ?.let { "https://flagcdn.com/w320/$it.png" }
        ?: flags?.png.orEmpty()

@Serializable
data class CountryItem(
    @SerialName("alpha2Code")
    val alpha2Code: String? = null,
    @SerialName("alpha3Code")
    val alpha3Code: String? = null,
    @SerialName("altSpellings")
    val altSpellings: List<String>? = null,
    @SerialName("area")
    val area: Double? = null,
    @SerialName("borders")
    val borders: List<String>? = null,
    @SerialName("callingCodes")
    val callingCodes: List<String>? = null,
    @SerialName("capital")
    val capital: String? = null,
    @SerialName("cioc")
    val cioc: String? = null,
    @SerialName("currencies")
    val currencies: List<Currency>? = null,
    @SerialName("demonym")
    val demonym: String? = null,
    @SerialName("flag")
    val flag: String? = null,
    @SerialName("flags")
    val flags: Flags? = null,
    @SerialName("gini")
    val gini: Double? = null,
    @SerialName("independent")
    val independent: Boolean? = null,
    @SerialName("languages")
    val languages: List<Language>? = null,
    @SerialName("latlng")
    val latlng: List<Double>? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("nativeName")
    val nativeName: String? = null,
    @SerialName("numericCode")
    val numericCode: String? = null,
    @SerialName("population")
    val population: Int? = null,
    @SerialName("region")
    val region: String? = null,
    @SerialName("regionalBlocs")
    val regionalBlocs: List<RegionalBloc>? = null,
    @SerialName("subregion")
    val subregion: String? = null,
    @SerialName("timezones")
    val timezones: List<String>? = null,
    @SerialName("topLevelDomain")
    val topLevelDomain: List<String>? = null,
    @SerialName("translations")
    val translations: Translations? = null,
)

@Serializable
data class Currency(
    @SerialName("code")
    val code: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("symbol")
    val symbol: String?,
)

@Serializable
data class Flags(
    @SerialName("png")
    val png: String?,
    @SerialName("svg")
    val svg: String?,
)

@Serializable
data class Language(
    @SerialName("iso639_1")
    val iso639_1: String?,
    @SerialName("iso639_2")
    val iso639_2: String,
    @SerialName("name")
    val name: String?,
    @SerialName("nativeName")
    val nativeName: String?,
)

@Serializable
data class RegionalBloc(
    @SerialName("acronym")
    val acronym: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("otherAcronyms")
    val otherAcronyms: List<String?>?,
    @SerialName("otherNames")
    val otherNames: List<String?>?,
)

@Serializable
data class Translations(
    @SerialName("br")
    val br: String?,
    @SerialName("de")
    val de: String?,
    @SerialName("es")
    val es: String?,
    @SerialName("fa")
    val fa: String?,
    @SerialName("fr")
    val fr: String?,
    @SerialName("hr")
    val hr: String?,
    @SerialName("hu")
    val hu: String?,
    @SerialName("it")
    val `it`: String?,
    @SerialName("ja")
    val ja: String?,
    @SerialName("nl")
    val nl: String?,
    @SerialName("pt")
    val pt: String?,
)
