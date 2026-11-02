package com.antigravity.wanderingwatch.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/**
 * Outer glowing halo border matching the sleek curvature of the Pixel 9a bezel.
 * Emits a visible, multi-layered ambient white neon bloom into the pitch-black OLED display.
 */
@Composable
fun OuterHaloContainer(
    modifier: Modifier = Modifier,
    isNightDimmed: Boolean = false,
    content: @Composable () -> Unit
) {
    val haloAlpha = if (isNightDimmed) 0.40f else 1.0f
    val glowAlpha = if (isNightDimmed) 0.12f else 0.45f

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val cornerRadiusPx = 22.dp.toPx()
                val marginPx = 10.dp.toPx()

                val haloSize = Size(
                    width = size.width - (marginPx * 2),
                    height = size.height - (marginPx * 2)
                )
                val haloOffset = Offset(marginPx, marginPx)

                // Pass 1: Wide diffused ambient bloom
                drawRoundRect(
                    color = Color.White.copy(alpha = glowAlpha * 0.25f),
                    topLeft = Offset(marginPx - 6.dp.toPx(), marginPx - 6.dp.toPx()),
                    size = Size(haloSize.width + 12.dp.toPx(), haloSize.height + 12.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadiusPx + 6.dp.toPx()),
                    style = Stroke(width = 14.dp.toPx())
                )

                // Pass 2: Medium diffused bloom
                drawRoundRect(
                    color = Color.White.copy(alpha = glowAlpha * 0.50f),
                    topLeft = Offset(marginPx - 3.dp.toPx(), marginPx - 3.dp.toPx()),
                    size = Size(haloSize.width + 6.dp.toPx(), haloSize.height + 6.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadiusPx + 3.dp.toPx()),
                    style = Stroke(width = 8.dp.toPx())
                )

                // Pass 3: Tight radiant glow
                drawRoundRect(
                    color = Color.White.copy(alpha = glowAlpha * 0.85f),
                    topLeft = Offset(marginPx - 1.5.dp.toPx(), marginPx - 1.5.dp.toPx()),
                    size = Size(haloSize.width + 3.dp.toPx(), haloSize.height + 3.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadiusPx + 1.5.dp.toPx()),
                    style = Stroke(width = 4.5.dp.toPx())
                )

                // Pass 4: Core crisp white neon rim
                drawRoundRect(
                    color = Color.White.copy(alpha = haloAlpha),
                    topLeft = haloOffset,
                    size = haloSize,
                    cornerRadius = CornerRadius(cornerRadiusPx),
                    style = Stroke(width = 3.dp.toPx())
                )
            }
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        content()
    }
}
