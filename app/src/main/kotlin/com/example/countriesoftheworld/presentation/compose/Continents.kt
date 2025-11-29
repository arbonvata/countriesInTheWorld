package com.example.countriesoftheworld.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

data class Continent(
    val name: String,
    val description: String,
    val emoji: String,
)

// Step 2: Create a list of continents. This is our data source.
private val continentsList =
    listOf(
        Continent(
            "Asia",
            "The largest and most populous continent, home to diverse cultures and rapidly growing economies.",
            "🌏",
        ),
        Continent(
            "Africa",
            "The world's second-largest and second-most populous continent, known for its rich history and wildlife.",
            "🌍",
        ),
        Continent(
            "North America",
            "A continent comprising Canada, the United States, Mexico, and the Caribbean islands.",
            "🌎",
        ),
        Continent(
            "South America",
            "A continent known for the Amazon rainforest, the Andes mountains, and vibrant cultures.",
            "🌎",
        ),
        Continent(
            "Antarctica",
            "The coldest, driest, and highest continent, almost entirely covered in ice.",
            "🧊",
        ),
        Continent(
            "Europe",
            "A continent known for its significant influence on global culture, history, and politics.",
            "🌍",
        ),
        Continent(
            "Australia (Oceania)",
            "A continent comprising Australia, New Zealand, and numerous islands in the Pacific Ocean.",
            "🌏",
        ),
    )

// Step 3: Create a composable for a single item in the list
@Composable
fun ContinentItem(
    continent: Continent,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = continent.emoji,
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 16.dp),
            )
            Column {
                Text(
                    text = continent.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = continent.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

// Step 4: Create the main screen composable (without Scaffold)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContinentsScreen(
    onContinentClick: (String) -> Unit,
    onBackClick: () -> Boolean,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "World Continents")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        },
    ) { paddingValues ->
        // LazyColumn is used for efficiently displaying a scrollable list
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier.fillMaxSize(),
        ) {
            // The items() function iterates over our list and creates a composable for each item
            items(continentsList) { continent ->
                ContinentItem(
                    continent = continent,
                    onClick = { onContinentClick(continent.name) },
                )
            }
        }
    }
}

// Step 5: Create a Preview to see the UI in Android Studio
@Preview(showBackground = true)
@Composable
fun ContinentsScreenPreview() {
    CountriesOfTheWorldTheme {
        // Use your app's theme
        ContinentsScreen(
            onContinentClick = { },
            onBackClick = { true },
        )
    }
}
