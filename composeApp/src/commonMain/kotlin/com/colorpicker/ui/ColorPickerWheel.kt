package com.colorpicker.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.*

/**
 * A Hue/Saturation color picker rendered as a square gradient.
 * Hue runs along X axis (0-360), Saturation along Y axis (1.0 top -> 0.0 bottom).
 * Value is controlled externally via a slider.
 */
@Composable
fun ColorPickerPanel(
    value: Float,
    selectedHue: Float,
    selectedSaturation: Float,
    onColorSelected: (hue: Float, saturation: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var panelSize by remember { mutableStateOf(Size.Zero) }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .pointerInput(value) {
                    detectTapGestures { offset ->
                        val hue = (offset.x / size.width).coerceIn(0f, 1f) * 360f
                        val saturation = (1f - offset.y / size.height).coerceIn(0f, 1f)
                        onColorSelected(hue, saturation)
                    }
                }
                .pointerInput(value) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val hue = (change.position.x / size.width).coerceIn(0f, 1f) * 360f
                        val saturation = (1f - change.position.y / size.height).coerceIn(0f, 1f)
                        onColorSelected(hue, saturation)
                    }
                }
        ) {
            panelSize = size
            drawHueSaturationPanel(value)

            // Draw selection indicator
            val cx = (selectedHue / 360f) * size.width
            val cy = (1f - selectedSaturation) * size.height
            drawCircle(
                color = Color.White,
                radius = 12f,
                center = Offset(cx, cy),
                style = Stroke(width = 3f)
            )
            drawCircle(
                color = Color.Black,
                radius = 14f,
                center = Offset(cx, cy),
                style = Stroke(width = 1.5f)
            )
        }
    }
}

private fun DrawScope.drawHueSaturationPanel(value: Float) {
    val width = size.width.toInt()
    val height = size.height.toInt()
    val cellW = max(1, width / 60)
    val cellH = max(1, height / 40)

    for (x in 0 until width step cellW) {
        for (y in 0 until height step cellH) {
            val hue = (x.toFloat() / width) * 360f
            val saturation = 1f - (y.toFloat() / height)
            val color = Color.hsv(hue.coerceIn(0f, 360f), saturation.coerceIn(0f, 1f), value)
            drawRect(
                color = color,
                topLeft = Offset(x.toFloat(), y.toFloat()),
                size = Size(cellW.toFloat() + 1, cellH.toFloat() + 1)
            )
        }
    }
}

/**
 * Convert HSV to RGB integer components.
 */
fun hsvToRgb(hue: Float, saturation: Float, value: Float): Triple<Int, Int, Int> {
    val h = hue / 60f
    val c = value * saturation
    val x = c * (1 - abs(h % 2 - 1))
    val m = value - c

    val (r1, g1, b1) = when {
        h < 1 -> Triple(c, x, 0f)
        h < 2 -> Triple(x, c, 0f)
        h < 3 -> Triple(0f, c, x)
        h < 4 -> Triple(0f, x, c)
        h < 5 -> Triple(x, 0f, c)
        else  -> Triple(c, 0f, x)
    }

    return Triple(
        ((r1 + m) * 255).roundToInt().coerceIn(0, 255),
        ((g1 + m) * 255).roundToInt().coerceIn(0, 255),
        ((b1 + m) * 255).roundToInt().coerceIn(0, 255)
    )
}

private fun Float.roundToInt(): Int = (this + 0.5f).toInt()
