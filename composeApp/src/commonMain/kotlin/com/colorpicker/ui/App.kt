package com.colorpicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colorpicker.domain.ColorMatcher
import com.colorpicker.domain.PaletteColor
import com.colorpicker.domain.predefinedPalette

@Composable
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ColorPickerScreen()
        }
    }
}

@Composable
private fun ColorPickerScreen() {
    var hue by remember { mutableFloatStateOf(0f) }
    var saturation by remember { mutableFloatStateOf(1f) }
    var value by remember { mutableFloatStateOf(1f) }

    val (r, g, b) = hsvToRgb(hue, saturation, value)
    val selectedColor = Color(r, g, b)
    val hexString = "#" +
        r.toString(16).padStart(2, '0') +
        g.toString(16).padStart(2, '0') +
        b.toString(16).padStart(2, '0')

    val closestColor = remember(r, g, b) {
        ColorMatcher.findClosest(r, g, b, predefinedPalette)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Color Picker",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        // Color picker panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ColorPickerPanel(
                    value = value,
                    selectedHue = hue,
                    selectedSaturation = saturation,
                    onColorSelected = { h, s ->
                        hue = h
                        saturation = s
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Brightness slider
                Text(
                    text = "Brightness",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Slider(
                    value = value,
                    onValueChange = { value = it },
                    valueRange = 0f..1f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = selectedColor,
                        activeTrackColor = selectedColor
                    )
                )
            }
        }

        // Results cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Selected color card
            ColorResultCard(
                title = "Selected",
                color = selectedColor,
                hex = hexString,
                modifier = Modifier.weight(1f)
            )

            // Closest palette color card
            if (closestColor != null) {
                val matchColor = Color(closestColor.r, closestColor.g, closestColor.b)
                ColorResultCard(
                    title = "Closest Match",
                    color = matchColor,
                    hex = closestColor.hex,
                    modifier = Modifier.weight(1f),
                    highlighted = true
                )
            }
        }

        // Palette section
        Text(
            text = "Color Palette",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        PaletteGrid(
            palette = predefinedPalette,
            closestColor = closestColor,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ColorResultCard(
    title: String,
    color: Color,
    hex: String,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false
) {
    Card(
        modifier = modifier.then(
            if (highlighted) Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) else Modifier
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(color)
                    .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape)
            )

            Text(
                text = hex.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PaletteGrid(
    palette: List<PaletteColor>,
    closestColor: PaletteColor?,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 44.dp),
        modifier = modifier.heightIn(max = 400.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(palette, key = { it.hex }) { paletteColor ->
            val isMatch = paletteColor.hex == closestColor?.hex
            val color = Color(paletteColor.r, paletteColor.g, paletteColor.b)

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(if (isMatch) 8.dp else 2.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
                    .then(
                        if (isMatch) Modifier.border(
                            width = 3.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isMatch) {
                    Text(
                        text = "✓",
                        color = if (paletteColor.r * 0.299 + paletteColor.g * 0.587 + paletteColor.b * 0.114 > 150)
                            Color.Black else Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
