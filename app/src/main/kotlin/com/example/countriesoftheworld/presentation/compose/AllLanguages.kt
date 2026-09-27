package com.example.countriesoftheworld.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.countriesoftheworld.R
import com.example.countriesoftheworld.data.model.LanguageData
import com.example.countriesoftheworld.presentation.viewmodel.LanguageViewModel
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

@Composable
fun AllLanguagesScreen(
    viewModel: LanguageViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onLanguageClick: (String) -> Unit = {}, // Add callback for language clicks
) {
    val languages by viewModel.filteredLanguages.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    AllLanguagesContent(
        languages = languages,
        searchQuery = searchQuery,
        onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
        onBackClick = onBackClick,
        onLanguageClick = onLanguageClick, // Pass the callback
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllLanguagesContent(
    languages: List<LanguageData>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onLanguageClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Languages",
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
        Column(modifier = Modifier.padding(innerPadding)) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                placeholder = { Text(stringResource(R.string.search_for_a_language)) },
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(languages) { language ->
                    Text(
                        text = language.name,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    onLanguageClick(language.code) // Pass the language code when clicked
                                },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AllLanguagesPreview() {
    CountriesOfTheWorldTheme {
        AllLanguagesContent(
            languages =
                listOf(
                    LanguageData(code = "en", name = "English", native = "English"),
                    LanguageData(code = "fr", name = "French", native = "Français"),
                    LanguageData(code = "es", name = "Spanish", native = "Español"),
                ),
            searchQuery = "",
            onSearchQueryChanged = {},
            onBackClick = {},
            onLanguageClick = {},
        )
    }
}
