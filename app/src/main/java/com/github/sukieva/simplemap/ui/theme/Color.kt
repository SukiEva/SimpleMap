package com.github.sukieva.simplemap.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
const val AlphaNearOpaque = 0.95f

val Colors.canvas: Color @Composable get() = if (isLight) Black else White