package com.colorpicker.domain

val predefinedPalette: List<PaletteColor> = listOf(
    // Reds / Pinks
    "#ffcdd2", "#e57373", "#d32f2f", "#f06292", "#c2185b",
    "#880e4f", "#f50057",
    // Purples
    "#e1bee7", "#ba68c8", "#8e24aa", "#aa00ff",
    // Deep Purples / Indigos
    "#7e57c2", "#4527a0", "#7c4dff", "#6200ea",
    "#c5cae9", "#7986cb", "#3949ab", "#304ffe",
    // Blues
    "#e3f2fd", "#64b5f6", "#1976d2", "#0288d1",
    "#40c4ff", "#00b0ff",
    // Cyans / Teals
    "#80deea", "#00acc1", "#00838f", "#006064",
    "#e0f2f1", "#80cbc4", "#00695c", "#64ffda",
    // Greens
    "#c8e6c9", "#66bb6a", "#2e7d32", "#1b5e20", "#00e676",
    // Light Greens / Limes
    "#aed581", "#689f38", "#33691e", "#76ff03", "#64dd17",
    "#cddc39", "#9e9d24", "#827717",
    // Yellows
    "#fff9c4", "#fbc02d", "#f57f17", "#ffff00",
    // Oranges / Browns
    "#ffcc80", "#ffccbc", "#ffab91",
    "#bcaaa4", "#8d6e63", "#4e342e",
    // Greys
    "#fafafa", "#bdbdbd", "#757575", "#424242",
    // Blue Greys
    "#cfd8dc", "#b0bec5", "#607d8b", "#37474f",
    // User settings old styles
    "#ea5911", "#467e4a", "#276696", "#b40303"
).map { PaletteColor.fromHex(it) }
