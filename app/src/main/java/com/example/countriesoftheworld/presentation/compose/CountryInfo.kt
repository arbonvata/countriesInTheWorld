package com.example.countriesoftheworld.presentation.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.example.countriesoftheworld.presentation.viewmodel.CountryViewModel
import com.example.countriesoftheworld.presentation.viewmodel.SingleCountryUiState
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

const val TAG = "CountryInfoScreen"

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
    LaunchedEffect(Unit) {
        Log.d(TAG, "LaunchedEffect: Fetching country details for $countryName")

        countryViewModel.fetchCountryByName(countryName)
    }
    val countryState = country
    // Todo: Fix scrolling
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (countryState.value) {
            is SingleCountryUiState.Success -> {
                val countryState = countryState.value as SingleCountryUiState.Success
                val country = countryState.country

                CountryView(title = "Country", info = country.name ?: "N/A")
                Spacer(modifier = Modifier.height(16.dp))
                CountryView(title = "Capital", info = country.capital ?: "N/A")
                Spacer(modifier = Modifier.height(16.dp))

                val currenciesInfo =
                    country.currencies
                        ?.mapNotNull { it.name }
                        ?.joinToString(separator = " ")
                        ?: "N/A"
                CountryView(title = "Currencies", info = currenciesInfo)
                Spacer(modifier = Modifier.height(16.dp))

                CountryInfoWithList(
                    modifier = modifier.fillMaxWidth(),
                    title = "Borders",
                    info = country.borders ?: emptyList(),
                )
                Spacer(modifier = Modifier.height(16.dp))

                val languagesInfo =
                    country.languages
                        ?.mapNotNull { it.name }
                        ?: emptyList()
                CountryInfoWithList(
                    modifier = modifier.fillMaxWidth(),
                    title = "Languages",
                    info = languagesInfo,
                )
                Spacer(modifier = Modifier.height(16.dp))

                val callingCode =
                    country.callingCodes
                        ?.firstOrNull()
                        ?: "N/A"
                CountryView(title = "Calling code", info = callingCode)
                Spacer(modifier = Modifier.height(16.dp))
                CountryView(title = "Region", info = country.region ?: "N/A")
                Spacer(modifier = Modifier.height(16.dp))
                CountryView(title = "Sub region", info = country.subregion ?: "N/A")
                Spacer(modifier = Modifier.height(16.dp))
                CountryView(title = "Population", info = country.population?.toString() ?: "N/A")
                Spacer(modifier = Modifier.height(16.dp))
                CountryView(title = "Area", info = country.area?.toString() ?: "N/A")
                Spacer(modifier = Modifier.height(16.dp))

                TextWithFlag(
                    modifier = modifier,
                    countryName = country.name ?: "Unknown",
                    flagUrl = country.flag ?: "",
                )
            }

            is SingleCountryUiState.Error -> {
                val countryState = countryState.value as SingleCountryUiState.Error
                Log.e(TAG, "Error fetching country details: ${countryState.message}")
            }

            is SingleCountryUiState.Loading -> {
                val countryState = countryState.value as SingleCountryUiState.Loading
                Log.d(TAG, "Loading country details...")
            }

            SingleCountryUiState.NotFound -> {
                Log.d(TAG, "Country not found")
            }
        }
    }
}

@Composable
fun CountryView(
    modifier: Modifier = Modifier,
    title: String,
    info: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    // Copy existing style to modify it
                    fontWeight = FontWeight.Bold, // Make the text bold
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
    modifier: Modifier = Modifier,
    title: String,
    info: List<String>,
) {
    if (info.isEmpty()) {
        return
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
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
    modifier: Modifier,
    countryName: String,
    flagUrl: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            // Provide some constraints for preview
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            // Provide some constraints
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
            modifier = Modifier.fillMaxWidth(),
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
            modifier = Modifier.fillMaxWidth(),
            title = "Region",
            info = "Americas, North America, Northern America",
        )
    }
}
