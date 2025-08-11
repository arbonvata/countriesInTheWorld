package com.example.countriesoftheworld.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountryItem(
    @SerialName("alpha2Code")
    val alpha2Code: String? = null,
    @SerialName("alpha3Code")
    val alpha3Code: String? = null,
    @SerialName("al?Spellings")
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
    val independent: Boolean?,
    @SerialName("languages")
    val languages: List<Language>?,
    @SerialName("latlng")
    val latlng: List<Double>?,
    @SerialName("name")
    val name: String?,
    @SerialName("nativeName")
    val nativeName: String?,
    @SerialName("numericCode")
    val numericCode: String?,
    @SerialName("population")
    val population: Int?,
    @SerialName("region")
    val region: String?,
    @SerialName("regionalBlocs")
    val regionalBlocs: List<RegionalBloc>?,
    @SerialName("subregion")
    val subregion: String?,
    @SerialName("timezones")
    val timezones: List<String>?,
    @SerialName("topLevelDomain")
    val topLevelDomain: List<String>?,
    @SerialName("translations")
    val translations: Translations?,
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
