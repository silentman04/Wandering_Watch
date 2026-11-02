package com.antigravity.wanderingwatch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.antigravity.wanderingwatch.ui.theme.DarkFlipCardBg
import com.antigravity.wanderingwatch.ui.theme.FlipCardBorder
import com.antigravity.wanderingwatch.ui.theme.PureWhite

/**
 * High-contrast dark flip card with mechanical hinges, split seam, and 1-bit pixel numeral.
 */
@Composable
fun FlipCardDigit(
    digit: String,
    modifier: Modifier = Modifier
) {
    val char = digit.firstOrNull() ?: '0'

    Box(
        modifier = modifier
            .width(86.dp)
            .height(156.dp)
            .background(DarkFlipCardBg, shape = RoundedCornerShape(10.dp))
            .border(2.dp, PureWhite.copy(alpha = 0.85f), shape = RoundedCornerShape(10.dp))
            .drawBehind {
                val midY = size.height / 2f
                val hingeWidth = 6.dp.toPx()
                val hingeHeight = 10.dp.toPx()

                // Draw 1-bit pixel numeral centered on card
                val pixelSize = 9.dp.toPx()
                PixelGlyphs.drawLargeDigit(
                    drawScope = this,
                    char = char,
                    centerX = size.width / 2f,
                    centerY = midY,
                    pixelSize = pixelSize,
                    color = PureWhite
                )

                // Left mechanical hinge notch
                drawRect(
                    color = PureWhite,
                    topLeft = Offset(0f, midY - hingeHeight / 2f),
                    size = Size(hingeWidth, hingeHeight)
                )

                // Right mechanical hinge notch
                drawRect(
                    color = PureWhite,
                    topLeft = Offset(size.width - hingeWidth, midY - hingeHeight / 2f),
                    size = Size(hingeWidth, hingeHeight)
                )

                // Central horizontal split seam (cuts cleanly through card and number)
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, midY),
                    end = Offset(size.width, midY),
                    strokeWidth = 3.dp.toPx()
                )
                drawLine(
                    color = FlipCardBorder,
                    start = Offset(0f, midY + 1.dp.toPx()),
                    end = Offset(size.width, midY + 1.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            }
    )
}

/**
 * Pixel colon separator between hours and minutes.
 */
@Composable
fun FlipColonSeparator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(26.dp)
            .height(156.dp)
            .drawBehind {
                val p = 4.dp.toPx()
                val midX = size.width / 2f
                val midY = size.height / 2f

                // Top square pixel dot
                drawRect(
                    color = PureWhite,
                    topLeft = Offset(midX - p, midY - 22.dp.toPx()),
                    size = Size(2 * p, 2 * p)
                )
                // Bottom square pixel dot
                drawRect(
                    color = PureWhite,
                    topLeft = Offset(midX - p, midY + 14.dp.toPx()),
                    size = Size(2 * p, 2 * p)
                )
            }
    )
}

/**
 * Compact docked seconds badge positioned at the lower-right corner of the minute card.
 * Strictly displays the 2 digits without colon (e.g. "59").
 */
@Composable
fun DockedSecondsBadge(
    seconds: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(52.dp)
            .height(44.dp)
            .background(DarkFlipCardBg, shape = RoundedCornerShape(8.dp))
            .border(1.5.dp, PureWhite.copy(alpha = 0.85f), shape = RoundedCornerShape(8.dp))
            .drawBehind {
                val p = 2.4.dp.toPx()
                val spacing = 2.dp.toPx()
                val textW = PixelGlyphs.measureSmallTextWidth(seconds, p, spacing)
                val textH = 7 * p

                val startX = (size.width - textW) / 2f
                val startY = (size.height - textH) / 2f

                PixelGlyphs.drawSmallText(
                    drawScope = this,
                    text = seconds,
                    startX = startX,
                    startY = startY,
                    pixelSize = p,
                    spacing = spacing,
                    color = PureWhite
                )

                // Subtle split line
                val midY = size.height / 2f
                drawLine(
                    color = Color.Black.copy(alpha = 0.6f),
                    start = Offset(0f, midY),
                    end = Offset(size.width, midY),
                    strokeWidth = 1.dp.toPx()
                )
            }
    )
}
