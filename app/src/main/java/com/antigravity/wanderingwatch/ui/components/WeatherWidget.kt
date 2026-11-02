package com.antigravity.wanderingwatch.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.antigravity.wanderingwatch.ui.theme.DarkFlipCardBg
import com.antigravity.wanderingwatch.ui.theme.PureWhite

/**
 * Top-right weather widget featuring authentic 1-bit pixel art:
 * Crescent moon, stepped pixel clouds, and twinkling stars, cleanly clipped inside the frame.
 */
@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier
) {
    // Subtle, low-power ambient drift for cloud layer
    val infiniteTransition = rememberInfiniteTransition(label = "CloudDrift")
    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CloudOffset"
    )

    Box(
        modifier = modifier
            .size(width = 122.dp, height = 92.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkFlipCardBg)
            .border(2.dp, PureWhite, shape = RoundedCornerShape(12.dp))
            .drawBehind {
                val p = 2.0.dp.toPx()
                val w = size.width
                val h = size.height

                // 1. Pixel Art Stars
                fun drawPixelStar(x: Float, y: Float) {
                    drawRect(PureWhite, Offset(x, y - p), Size(p, p))
                    drawRect(PureWhite, Offset(x - p, y), Size(3 * p, p))
                    drawRect(PureWhite, Offset(x, y + p), Size(p, p))
                }
                drawPixelStar(w * 0.18f, h * 0.22f)
                drawPixelStar(w * 0.82f, h * 0.20f)
                drawRect(PureWhite, Offset(w * 0.35f, h * 0.32f), Size(p, p))
                drawRect(PureWhite, Offset(w * 0.88f, h * 0.45f), Size(p, p))

                // 2. Stepped 1-Bit Pixel Art Crescent Moon
                val moonStartX = w * 0.48f
                val moonStartY = h * 0.14f

                val moonBitmask = arrayOf(
                    "....1111..",
                    "..1111111.",
                    ".11111....",
                    "1111......",
                    "1111......",
                    "1111......",
                    "1111......",
                    ".11111....",
                    "..1111111.",
                    "....111111",
                    "......111."
                )

                for (r in moonBitmask.indices) {
                    val line = moonBitmask[r]
                    for (c in line.indices) {
                        if (line[c] == '1') {
                            drawRect(
                                color = PureWhite,
                                topLeft = Offset(moonStartX + c * (2.2f * p), moonStartY + r * (2.2f * p)),
                                size = Size(2.2f * p, 2.2f * p)
                            )
                        }
                    }
                }

                // 3. Stepped 1-Bit Pixel Clouds (nested cleanly in the lower half of the box)
                val cloudBaseX = (w * 0.12f) + (cloudOffset * p)
                val cloudBaseY = h * 0.52f

                // Pixel cloud silhouette blocks
                fun drawCloudBlock(x: Float, y: Float, blockW: Float, blockH: Float) {
                    drawRect(
                        color = PureWhite,
                        topLeft = Offset(x, y),
                        size = Size(blockW, blockH)
                    )
                }

                // Tier 1: Main base
                drawCloudBlock(cloudBaseX, cloudBaseY + 12 * p, 44 * p, 10 * p)
                drawCloudBlock(cloudBaseX + 6 * p, cloudBaseY + 6 * p, 32 * p, 16 * p)

                // Tier 2: Fluffy mounds
                drawCloudBlock(cloudBaseX + 12 * p, cloudBaseY, 20 * p, 22 * p)
                drawCloudBlock(cloudBaseX + 26 * p, cloudBaseY + 2 * p, 16 * p, 18 * p)

                // Secondary right cloud puff
                drawCloudBlock(cloudBaseX + 32 * p, cloudBaseY + 8 * p, 18 * p, 12 * p)

                // Inner detail horizontal grooves (characteristic of 1-bit retro pixel art)
                drawLine(
                    color = Color.Black,
                    start = Offset(cloudBaseX + 8 * p, cloudBaseY + 10 * p),
                    end = Offset(cloudBaseX + 24 * p, cloudBaseY + 10 * p),
                    strokeWidth = 1.8f * p
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(cloudBaseX + 22 * p, cloudBaseY + 15 * p),
                    end = Offset(cloudBaseX + 38 * p, cloudBaseY + 15 * p),
                    strokeWidth = 1.8f * p
                )
            }
    )
}
