package com.antigravity.wanderingwatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.antigravity.wanderingwatch.R
import com.antigravity.wanderingwatch.ui.components.PixelGlyphs
import com.antigravity.wanderingwatch.ui.theme.PureBlack
import com.antigravity.wanderingwatch.ui.theme.PureWhite
import com.antigravity.wanderingwatch.viewmodel.ClockViewModel

/**
 * 1:1 Static Artwork Screen with real-time live clock overlay and Day/Night celestial transition.
 * - Daytime (6:00 AM - 5:59 PM): Radiant pixel sun over fluffy clouds.
 * - Nighttime (6:00 PM - 5:59 AM): Crescent moon over clouds with starlight.
 * - Live hours, minutes, seconds, date, day, and AM/PM badges.
 */
@Composable
fun StaticDeskClockScreen(
    viewModel: ClockViewModel,
    modifier: Modifier = Modifier
) {
    val timeState by viewModel.timeState.collectAsState()
    val burnInState by viewModel.burnInState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack),
        contentAlignment = Alignment.Center
    ) {
        // Fullscreen edge-to-edge container wrapping the display perimeter
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 2.dp)
                .graphicsLayer {
                    // AMOLED burn-in protection: micro-orbiting & cyclic luminance breathing
                    translationX = burnInState.shiftX
                    translationY = burnInState.shiftY
                    alpha = burnInState.luminanceFactor
                }
        ) {
            // 1. Scaled 1:1 Base Artwork with automatic Day/Night celestial swap
            val baseArtDrawable = if (timeState.isDaytime) {
                R.drawable.clock_template_day
            } else {
                R.drawable.clock_template_night
            }

            Image(
                painter = painterResource(id = baseArtDrawable),
                contentDescription = "Desk Clock Artwork",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // 2. Real-time Live Time, Date, and AM/PM Overlay on Canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val w = size.width
                        val h = size.height

                        // Proportional scaling factors based on cropped 1272 x 660 template
                        val largePixelSize = (h / 660f) * 11.2f
                        val smallPixelSize = (h / 660f) * 4.6f
                        val datePixelSize = (h / 660f) * 5.4f
                        val amPmPixelSize = (h / 660f) * 3.8f

                        // A. LIVE FLIP CLOCK DIGITS (10:09)
                        val cardY = 0.5000f * h

                        // Hour Tens (Card 1)
                        val card1X = 0.3066f * w
                        val hTens = timeState.hourTens.firstOrNull() ?: '1'
                        PixelGlyphs.drawLargeDigit(
                            drawScope = this,
                            char = hTens,
                            centerX = card1X,
                            centerY = cardY,
                            pixelSize = largePixelSize,
                            color = PureWhite
                        )

                        // Hour Ones (Card 2)
                        val card2X = 0.4167f * w
                        val hOnes = timeState.hourOnes.firstOrNull() ?: '0'
                        PixelGlyphs.drawLargeDigit(
                            drawScope = this,
                            char = hOnes,
                            centerX = card2X,
                            centerY = cardY,
                            pixelSize = largePixelSize,
                            color = PureWhite
                        )

                        // Minute Tens (Card 3)
                        val card3X = 0.5550f * w
                        val mTens = timeState.minuteTens.firstOrNull() ?: '0'
                        PixelGlyphs.drawLargeDigit(
                            drawScope = this,
                            char = mTens,
                            centerX = card3X,
                            centerY = cardY,
                            pixelSize = largePixelSize,
                            color = PureWhite
                        )

                        // Minute Ones (Card 4)
                        val card4X = 0.6651f * w
                        val mOnes = timeState.minuteOnes.firstOrNull() ?: '9'
                        PixelGlyphs.drawLargeDigit(
                            drawScope = this,
                            char = mOnes,
                            centerX = card4X,
                            centerY = cardY,
                            pixelSize = largePixelSize,
                            color = PureWhite
                        )

                        // B. LIVE SECONDS BADGE (59)
                        val secX = 0.7563f * w
                        val secY = 0.5970f * h
                        val spacing = smallPixelSize * 0.8f
                        val secTextW = PixelGlyphs.measureSmallTextWidth(timeState.seconds, smallPixelSize, spacing)
                        PixelGlyphs.drawSmallText(
                            drawScope = this,
                            text = timeState.seconds,
                            startX = secX - (secTextW / 2f),
                            startY = secY - (7 * smallPixelSize / 2f),
                            pixelSize = smallPixelSize,
                            spacing = spacing,
                            color = PureWhite
                        )

                        // C. LIVE DATE BADGE (e.g. "11/02")
                        val dateX = 0.1549f * w
                        val dateY = 0.1652f * h
                        val dateSpacing = datePixelSize * 0.7f
                        val dateTextW = PixelGlyphs.measureSmallTextWidth(timeState.date, datePixelSize, dateSpacing)
                        PixelGlyphs.drawSmallText(
                            drawScope = this,
                            text = timeState.date,
                            startX = dateX - (dateTextW / 2f),
                            startY = dateY - (7 * datePixelSize / 2f),
                            pixelSize = datePixelSize,
                            spacing = dateSpacing,
                            color = PureWhite
                        )

                        // D. LIVE DAY BADGE (e.g. "MON")
                        val dayX = 0.3255f * w
                        val dayY = 0.1652f * h
                        val daySpacing = datePixelSize * 0.7f
                        val dayTextW = PixelGlyphs.measureSmallTextWidth(timeState.dayOfWeek, datePixelSize, daySpacing)
                        PixelGlyphs.drawSmallText(
                            drawScope = this,
                            text = timeState.dayOfWeek,
                            startX = dayX - (dayTextW / 2f),
                            startY = dayY - (7 * datePixelSize / 2f),
                            pixelSize = datePixelSize,
                            spacing = daySpacing,
                            color = PureWhite
                        )

                        // E. LIVE AM/PM BADGE
                        val amPmX = 0.0857f * w
                        val amPmY = 0.5545f * h
                        val amPmSpacing = amPmPixelSize * 0.6f
                        val amPmTextW = PixelGlyphs.measureSmallTextWidth(timeState.amPm, amPmPixelSize, amPmSpacing)
                        PixelGlyphs.drawSmallText(
                            drawScope = this,
                            text = timeState.amPm,
                            startX = amPmX - (amPmTextW / 2f),
                            startY = amPmY - (7 * amPmPixelSize / 2f),
                            pixelSize = amPmPixelSize,
                            spacing = amPmSpacing,
                            color = PureWhite
                        )
                    }
            )
        }
    }
}
