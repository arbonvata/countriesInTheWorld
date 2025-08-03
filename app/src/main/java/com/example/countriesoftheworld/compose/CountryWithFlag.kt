package com.example.countriesoftheworld.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.countriesoftheworld.data.model.Country
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

@Composable
fun CountryWithFlag(
    modifier: Modifier,
    country: Country,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = country.flagUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(44.dp).aspectRatio(1f),
        )
        Text(
            text = country.name,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
        // Add a spacer to push the icon to the right
        Spacer(modifier = Modifier.weight(1f))
        // Add the three-dot icon
        CountryOptionsMenu(
            expanded = expanded,
            checked = checked,
            onExpandedChange = {expanded = it},
            onCheckedChange = { newState ->
                checked = newState
                onCheckedChange(checked)
            }
        )
    }

    }

@Composable
fun CountryOptionsMenu(
    expanded: Boolean,
    checked: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    Box {
        IconButton(onClick = { onExpandedChange(true) }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { newState ->
                                onCheckedChange(newState)
                                onExpandedChange(false) // Close menu after selection
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = "Select Country")
                    }
                },
                onClick = { /* Handled by Checkbox */ }
            )
        }
    }
}

@Composable
@Preview
fun CountryWithFlagPreview() {
    CountriesOfTheWorldTheme {
        CountryWithFlag(country = Country("United States", "https://flagcdn.com/w320/us.png"), modifier = Modifier.padding(8.dp))
    }
}
