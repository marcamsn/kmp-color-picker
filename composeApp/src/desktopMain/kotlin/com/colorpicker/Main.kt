package com.colorpicker

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.colorpicker.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Color Picker - Closest Match",
        state = rememberWindowState(width = 480.dp, height = 800.dp)
    ) {
        App()
    }
}
