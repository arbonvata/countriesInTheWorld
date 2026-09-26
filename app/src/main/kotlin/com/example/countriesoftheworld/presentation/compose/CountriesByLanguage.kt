package com.example.countriesoftheworld.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.data.model.Flags
import com.example.countriesoftheworld.data.model.Language
import com.example.countriesoftheworld.data.model.flagImageUrl
import com.example.countriesoftheworld.presentation.viewmodel.CountriesByLanguageViewModel
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

@Composable
fun CountriesByLanguageScreen(
    languageCode: String,
    languageName: String = "",
    viewModel: CountriesByLanguageViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onCountryClick: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load countries when language code changes
    viewModel.loadCountriesByLanguage(languageCode)

    CountriesByLanguageContent(
        languageCode = languageCode,
        languageName = languageName,
        countries = uiState.countries,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onBackClick = onBackClick,
        onCountryClick = onCountryClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountriesByLanguageContent(
    languageCode: String,
    languageName: String,
    countries: List<CountryItem>,
    isLoading: Boolean,
    error: String?,
    onBackClick: () -> Unit,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            if (languageName.isNotEmpty()) {
                                "Countries where $languageName is spoken"
                            } else {
                                "Countries for language: $languageCode"
                            },
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                countries.isEmpty() -> {
                    Text(
                        text = "No countries found for this language",
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(countries) { country ->
                            CountryCardForLanguage(
                                country = country,
                                onCountryClick = { onCountryClick(country.name ?: "") },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountryCardForLanguage(
    country: CountryItem,
    onCountryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onCountryClick() },
        onClick = onCountryClick,
    ) {
        Column {
            AsyncImage(
                model = country.flagImageUrl(),
                contentDescription = "Flag of ${country.name}",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = country.name ?: "Unknown Country",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
            )

            // Show languages spoken in this country
            country.languages?.let { languages ->
                if (languages.isNotEmpty()) {
                    Text(
                        text = "Languages: ${languages.joinToString { it.name ?: "Unknown" }}",
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CountriesByLanguagePreview() {
    CountriesOfTheWorldTheme {
        CountriesByLanguageContent(
            languageCode = "en",
            languageName = "English",
            countries =
                listOf(
                    CountryItem(
                        name = "United States",
                        flags = Flags(png = "https://flagcdn.com/w320/us.png", svg = null),
                        languages = listOf(Language(name = "English", iso639_1 = "en", iso639_2 = "eng", nativeName = "English")),
                        alpha2Code = null,
                        alpha3Code = null,
                        altSpellings = null,
                        area = null,
                        borders = null,
                        callingCodes = null,
                        capital = null,
                        cioc = null,
                        currencies = null,
                        demonym = null,
                        flag = null,
                        gini = null,
                        independent = null,
                        latlng = null,
                        nativeName = null,
                        numericCode = null,
                        population = null,
                        region = null,
                        regionalBlocs = null,
                        subregion = null,
                        timezones = null,
                        topLevelDomain = null,
                        translations = null,
                    ),
                    CountryItem(
                        name = "United Kingdom",
                        flags = Flags(png = "https://flagcdn.com/w320/gb.png", svg = null),
                        languages = listOf(Language(name = "English", iso639_1 = "en", iso639_2 = "eng", nativeName = "English")),
                        alpha2Code = null,
                        alpha3Code = null,
                        altSpellings = null,
                        area = null,
                        borders = null,
                        callingCodes = null,
                        capital = null,
                        cioc = null,
                        currencies = null,
                        demonym = null,
                        flag = null,
                        gini = null,
                        independent = null,
                        latlng = null,
                        nativeName = null,
                        numericCode = null,
                        population = null,
                        region = null,
                        regionalBlocs = null,
                        subregion = null,
                        timezones = null,
                        topLevelDomain = null,
                        translations = null,
                    ),
                ),
            isLoading = false,
            error = null,
            onBackClick = {},
            onCountryClick = {},
        )
    }
}
