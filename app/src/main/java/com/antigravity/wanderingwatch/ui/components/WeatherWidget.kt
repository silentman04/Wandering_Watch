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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.antigravity.wanderingwatch.ui.theme.DarkFlipCardBg
import com.antigravity.wanderingwatch.ui.theme.PureWhite

/**
 * Top-right weather widget featuring pixel-art crescent moon, fluffy clouds, and twinkling stars.
 */
@Composable
fun WeatherWidget(
    modifier: Modifier = Modifier
) {
    // Subtle, low-power ambient drift for cloud layer
    val infiniteTransition = rememberInfiniteTransition(label = "CloudDrift")
    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CloudOffset"
    )

    Box(
        modifier = modifier
            .size(width = 118.dp, height = 86.dp)
            .background(DarkFlipCardBg, shape = RoundedCornerShape(10.dp))
            .border(1.5.dp, PureWhite, shape = RoundedCornerShape(10.dp))
            .drawBehind {
                val p = 2.dp.toPx()
                val centerX = size.width / 2f
                val centerY = size.height / 2f

                // 1. Twinkling stars in the night sky
                fun drawPixelStar(x: Float, y: Float) {
                    drawRect(PureWhite, Offset(x, y - p), Size(p, p))
                    drawRect(PureWhite, Offset(x - p, y), Size(3 * p, p))
                    drawRect(PureWhite, Offset(x, y + p), Size(p, p))
                }
                drawPixelStar(centerX - 32 * p, centerY - 20 * p)
                drawPixelStar(centerX + 26 * p, centerY - 22 * p)
                drawRect(PureWhite, Offset(centerX + 18 * p, centerY - 10 * p), Size(p, p))
                drawRect(PureWhite, Offset(centerX - 22 * p, centerY - 8 * p), Size(p, p))

                // 2. Crescent Moon (drawn in crisp white pixels)
                val moonX = centerX + 4 * p
                val moonY = centerY - 16 * p
                val moonPath = Path().apply {
                    moveTo(moonX + 10 * p, moonY)
                    cubicTo(
                        moonX + 2 * p, moonY,
                        moonX - 4 * p, moonY + 8 * p,
                        moonX, moonY + 22 * p
                    )
                    cubicTo(
                        moonX + 6 * p, moonY + 28 * p,
                        moonX + 18 * p, moonY + 26 * p,
                        moonX + 22 * p, moonY + 20 * p
                    )
                    cubicTo(
                        moonX + 12 * p, moonY + 22 * p,
                        moonX + 6 * p, moonY + 14 * p,
                        moonX + 10 * p, moonY
                    )
                    close()
                }
                drawPath(moonPath, color = PureWhite)

                // 3. Fluffy pixel clouds resting beneath the moon (with subtle drift)
                val cx = centerX + (cloudOffset * p * 0.5f)
                val cy = centerY + 4 * p

                fun drawCloudPuff(offsetX: Float, offsetY: Float, radiusWidth: Float, height: Float) {
                    drawRoundRect(
                        color = PureWhite,
                        topLeft = Offset(offsetX, offsetY),
                        size = Size(radiusWidth, height),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(height / 2f)
                    )
                }

                // Cloud silhouette blocks
                drawCloudPuff(cx - 36 * p, cy + 6 * p, 48 * p, 16 * p)
                drawCloudPuff(cx - 24 * p, cy, 32 * p, 20 * p)
                drawCloudPuff(cx - 8 * p, cy + 4 * p, 40 * p, 16 * p)
                drawCloudPuff(cx + 12 * p, cy + 8 * p, 24 * p, 12 * p)

                // Fluffy cloud highlights (pixel texture lines)
                drawLine(
                    color = Color.Black.copy(alpha = 0.8f),
                    start = Offset(cx - 20 * p, cy + 8 * p),
                    end = Offset(cx - 6 * p, cy + 8 * p),
                    strokeWidth = p
                )
                drawLine(
                    color = Color.Black.copy(alpha = 0.8f),
                    start = Offset(cx + 2 * p, cy + 10 * p),
                    end = Offset(cx + 18 * p, cy + 10 * p),
                    strokeWidth = p
                )
            }
    )
}
