package com.example.countriesoftheworld.presentation.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.countriesoftheworld.data.model.CountryItem
import com.example.countriesoftheworld.presentation.viewmodel.CountryViewModel
import com.example.countriesoftheworld.presentation.viewmodel.SingleCountryUiState
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

const val TAG = "CountryInfoScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryInfoScreen(
    modifier: Modifier = Modifier,
    countryName: String,
    countryViewModel: CountryViewModel = hiltViewModel(),
    navController: NavController,
) {
    SideEffect {
        Log.d(TAG, "Entered CountryInfoScreen for country: $countryName")
    }
    val country = countryViewModel.singleCountryState.collectAsState()
    val scrollState = rememberScrollState()
    LaunchedEffect(countryName) {
        Log.d(TAG, "LaunchedEffect: Fetching country details for $countryName")
        countryViewModel.clearSingleCountryState()
        countryViewModel.fetchCountryByName(countryName)
    }
    val countryState = country

    Scaffold(
        topBar = {
            CountryInfoTopAppBar(
                countryName = countryName,
                onNavigateUp = { navController.popBackStack() },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            when (countryState.value) {
                is SingleCountryUiState.Success -> {
                    val countryState = countryState.value as SingleCountryUiState.Success
                    val country = countryState.country

                    CountryDetailsContent(country = country)
                }

                is SingleCountryUiState.Error -> {
                    val countryState = countryState.value as SingleCountryUiState.Error
                    Log.e(TAG, "Error fetching country details: ${countryState.message}")
                    // Show error message to user with retry option
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Error loading country details. Please check your connection and try again.",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center,
                        )
                        Button(
                            onClick = { countryViewModel.fetchCountryByName(countryName) },
                        ) {
                            Text("Retry")
                        }
                    }
                }

                is SingleCountryUiState.Loading -> {
                    // Show loading indicator
                    CountryInfoLoadingIndicator()
                }

                SingleCountryUiState.NotFound -> {
                    Log.d(TAG, "Country not found")
                    Text(
                        text = "Country not found",
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryInfoTopAppBar(
    countryName: String,
    onNavigateUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = countryName)
        },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
    )
}

@Composable
fun CountryInfoLoadingIndicator() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Loading country details...",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun CountryDetailsContent(country: CountryItem) {
    // Country name
    CountryView(title = "Country", info = country.name ?: "N/A")
    Spacer(modifier = Modifier.height(16.dp))

    // Capital
    CountryView(title = "Capital", info = country.capital ?: "N/A")
    Spacer(modifier = Modifier.height(16.dp))

    // Currencies
    val currenciesInfo =
        country.currencies
            ?.mapNotNull { it.name }
            ?.joinToString(separator = " ")
            ?: "N/A"
    CountryView(title = "Currencies", info = currenciesInfo)
    Spacer(modifier = Modifier.height(16.dp))

    // Borders
    CountryInfoWithList(
        title = "Borders",
        info = country.borders ?: emptyList(),
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Languages
    val languagesInfo =
        country.languages
            ?.mapNotNull { it.name }
            ?: emptyList()
    CountryInfoWithList(
        title = "Languages",
        info = languagesInfo,
    )
    Spacer(modifier = Modifier.height(16.dp))

    // Calling code
    val callingCode =
        country.callingCodes
            ?.firstOrNull()
            ?: "N/A"
    CountryView(title = "Calling code", info = callingCode)
    Spacer(modifier = Modifier.height(16.dp))

    // Region
    CountryView(title = "Region", info = country.region ?: "N/A")
    Spacer(modifier = Modifier.height(16.dp))

    // Sub region
    CountryView(title = "Sub region", info = country.subregion ?: "N/A")
    Spacer(modifier = Modifier.height(16.dp))

    // Population
    CountryView(title = "Population", info = country.population?.toString() ?: "N/A")
    Spacer(modifier = Modifier.height(16.dp))

    // Area
    CountryView(title = "Area", info = country.area?.toString() ?: "N/A")
    Spacer(modifier = Modifier.height(16.dp))

    // Flag
    TextWithFlag(
        countryName = country.name ?: "Unknown",
        flagUrl = country.flag ?: "",
    )
}

@Composable
fun CountryView(
    title: String,
    info: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            textAlign = TextAlign.Center,
        )
        Text(
            text = info,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun CountryInfoWithList(
    title: String,
    info: List<String>,
) {
    if (info.isEmpty()) {
        return
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(6.dp))
        // to avoid nested scrolling
        info.forEach { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun CountryVisitedByMe(
    modifier: Modifier = Modifier,
    initialCheckedState: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    // Use remember and mutableStateOf to hold the state of the Switch
    var isChecked by remember { mutableStateOf(initialCheckedState) }

    Row(
        modifier = Modifier.padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = { newCheckedState ->
                isChecked = newCheckedState
                onCheckedChange(newCheckedState)
            },
        )
        Spacer(modifier = Modifier.width(14.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = "Visited by me",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun TextWithFlag(
    countryName: String,
    flagUrl: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = countryName,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        AsyncImage(
            model = flagUrl,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth(),
            alignment = Alignment.Center,
        )
    }
}

@Preview(showBackground = true, name = "CountryVisitedByMe - Unchecked")
@Composable
fun CountryVisitedByMeUncheckedPreview() {
    CountriesOfTheWorldTheme {
        CountryVisitedByMe(
            onCheckedChange = { /* Lambda for preview, can be empty or log */ },
            initialCheckedState = false,
        )
    }
}

@Preview(showBackground = true, name = "CountryVisitedByMe - Checked")
@Composable
fun CountryVisitedByMeCheckedPreview() {
    CountriesOfTheWorldTheme {
        CountryVisitedByMe(
            onCheckedChange = { /* Lambda for preview */ },
            initialCheckedState = true,
        )
    }
}

// Preview for the CountryInfoWithList composable
@Preview(showBackground = true, name = "CountryInfoWithList Languages Preview")
@Composable
fun CountryInfoWithListLanguagesPreview() {
    CountriesOfTheWorldTheme {
        CountryInfoWithList(
            title = "Languages",
            info = listOf("English", "Spanish", "French"),
        )
    }
}

@Preview(showBackground = true, name = "CountryInfoWithList Empty List Preview")
@Composable
fun CountryInfoWithListEmptyPreview() {
    CountriesOfTheWorldTheme {
        CountryInfoWithList(
            title = "Borders",
            info = emptyList(), // Test how it looks with no items
        )
    }
}

// Preview for the CountryView composable
@Preview(showBackground = true, name = "CountryView Preview")
@Composable
fun CountryViewPreview() {
    CountriesOfTheWorldTheme {
        // Apply your app's theme for consistent styling
        CountryView(
            title = "Capital",
            info = "Washington, D.C.",
        )
    }
}

@Preview(showBackground = true, name = "CountryView Long Info Preview")
@Composable
fun CountryViewLongInfoPreview() {
    CountriesOfTheWorldTheme {
        CountryView(
            title = "Region",
            info = "Americas, North America, Northern America",
        )
    }
}
