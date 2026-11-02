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
 * Outer glowing halo border matching the subtle corner curvature of the Pixel 9a bezel.
 * Emits an ambient white neon glow into the pitch-black OLED display.
 */
@Composable
fun OuterHaloContainer(
    modifier: Modifier = Modifier,
    isNightDimmed: Boolean = false,
    content: @Composable () -> Unit
) {
    val haloAlpha = if (isNightDimmed) 0.35f else 0.90f
    val glowAlpha = if (isNightDimmed) 0.08f else 0.22f

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val strokeWidth = 2.5.dp.toPx()
                val cornerRadiusPx = 28.dp.toPx()
                val marginPx = 14.dp.toPx()

                val haloSize = Size(
                    width = size.width - (marginPx * 2),
                    height = size.height - (marginPx * 2)
                )
                val haloOffset = Offset(marginPx, marginPx)

                // Outer diffused glow bloom layer 1 (wide)
                drawRoundRect(
                    color = Color.White.copy(alpha = glowAlpha * 0.4f),
                    topLeft = Offset(marginPx - 3.dp.toPx(), marginPx - 3.dp.toPx()),
                    size = Size(haloSize.width + 6.dp.toPx(), haloSize.height + 6.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadiusPx + 3.dp.toPx()),
                    style = Stroke(width = 8.dp.toPx())
                )

                // Middle diffused glow bloom layer 2
                drawRoundRect(
                    color = Color.White.copy(alpha = glowAlpha),
                    topLeft = Offset(marginPx - 1.5.dp.toPx(), marginPx - 1.5.dp.toPx()),
                    size = Size(haloSize.width + 3.dp.toPx(), haloSize.height + 3.dp.toPx()),
                    cornerRadius = CornerRadius(cornerRadiusPx + 1.5.dp.toPx()),
                    style = Stroke(width = 4.dp.toPx())
                )

                // Core crisp white halo ring
                drawRoundRect(
                    color = Color.White.copy(alpha = haloAlpha),
                    topLeft = haloOffset,
                    size = haloSize,
                    cornerRadius = CornerRadius(cornerRadiusPx),
                    style = Stroke(width = strokeWidth)
                )
            }
            .padding(horizontal = 28.dp, vertical = 22.dp)
    ) {
        content()
    }
}
