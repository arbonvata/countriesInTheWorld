package com.example.countriesoftheworld.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

@Composable
fun CountryView(
    modifier: Modifier = Modifier,
    title: String,
    info: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = info,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun CountryInfoWithList(
    modifier: Modifier,
    title: String,
    info: List<String>,
) {
    if (info.isEmpty()) {
        return
    }
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(items = info) { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun CountryVisitedByMe(
    modifier: Modifier = Modifier,
    initialCheckedState: Boolean,
    onCheckedChange: () -> Unit,
) {
    var checked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.padding(start = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
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
            modifier = Modifier.fillMaxWidth().height(200.dp), // Provide some constraints for preview
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
            modifier = Modifier.fillMaxWidth().height(100.dp), // Provide some constraints
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
