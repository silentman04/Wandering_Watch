package com.antigravity.wanderingwatch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.wanderingwatch.ui.theme.DarkFlipCardBg
import com.antigravity.wanderingwatch.ui.theme.FallbackPixelFont
import com.antigravity.wanderingwatch.ui.theme.FlipCardBorder
import com.antigravity.wanderingwatch.ui.theme.PixelFontFamily
import com.antigravity.wanderingwatch.ui.theme.PureWhite

/**
 * Individual flip-card digit container with horizontal center seam and side mechanical hinges.
 */
@Composable
fun FlipCardDigit(
    digit: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(82.dp)
            .height(152.dp)
            .background(DarkFlipCardBg, shape = RoundedCornerShape(8.dp))
            .border(1.5.dp, FlipCardBorder, shape = RoundedCornerShape(8.dp))
            .drawBehind {
                val midY = size.height / 2f
                val hingeWidth = 5.dp.toPx()
                val hingeHeight = 8.dp.toPx()

                // Left mechanical hinge notch
                drawRect(
                    color = Color.White.copy(alpha = 0.8f),
                    topLeft = Offset(0f, midY - hingeHeight / 2f),
                    size = Size(hingeWidth, hingeHeight)
                )

                // Right mechanical hinge notch
                drawRect(
                    color = Color.White.copy(alpha = 0.8f),
                    topLeft = Offset(size.width - hingeWidth, midY - hingeHeight / 2f),
                    size = Size(hingeWidth, hingeHeight)
                )

                // Central horizontal split seam
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, midY),
                    end = Offset(size.width, midY),
                    strokeWidth = 2.dp.toPx()
                )
                drawLine(
                    color = FlipCardBorder,
                    start = Offset(0f, midY + 1.dp.toPx()),
                    end = Offset(size.width, midY + 1.dp.toPx()),
                    strokeWidth = 1.dp.toPx()
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            fontFamily = PixelFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 88.sp,
            color = PureWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
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
            .width(22.dp)
            .height(152.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 8.dp, height = 36.dp)
                    .drawBehind {
                        // Top square pixel dot
                        drawRect(
                            color = PureWhite,
                            topLeft = Offset(0f, 0f),
                            size = Size(size.width, size.width)
                        )
                        // Bottom square pixel dot
                        drawRect(
                            color = PureWhite,
                            topLeft = Offset(0f, size.height - size.width),
                            size = Size(size.width, size.width)
                        )
                    }
            )
        }
    }
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
            .height(48.dp)
            .background(DarkFlipCardBg, shape = RoundedCornerShape(6.dp))
            .border(1.5.dp, FlipCardBorder, shape = RoundedCornerShape(6.dp))
            .drawBehind {
                val midY = size.height / 2f
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, midY),
                    end = Offset(size.width, midY),
                    strokeWidth = 1.5.dp.toPx()
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = seconds,
            fontFamily = PixelFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = PureWhite,
            textAlign = TextAlign.Center
        )
    }
}
