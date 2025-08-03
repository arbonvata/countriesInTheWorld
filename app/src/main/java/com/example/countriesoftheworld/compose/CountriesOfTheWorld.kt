package com.example.countriesoftheworld.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.countriesoftheworld.R
import com.example.countriesoftheworld.ui.theme.CountriesOfTheWorldTheme

@Composable
fun CountriesOfTheWorld(
    modifier: Modifier = Modifier,
    text: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.four_lines_foreground),
            contentDescription = "4 lines",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(34.dp)
                    .clip(RectangleShape),
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
@Preview
fun CountriesOfTheWorldPreview() {
    CountriesOfTheWorldTheme {
        CountriesOfTheWorld(text = "All Countries")
    }
}
