package com.antigravity.wanderingwatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.antigravity.wanderingwatch.R
import com.antigravity.wanderingwatch.ui.components.PixelGlyphs
import com.antigravity.wanderingwatch.ui.theme.PureBlack
import com.antigravity.wanderingwatch.ui.theme.PureWhite
import com.antigravity.wanderingwatch.viewmodel.ClockViewModel

/**
 * 1:1 Static Artwork Screen with real-time live clock overlay.
 * Uses the exact approved design v7 artwork as a base, overlaying live hours, minutes,
 * seconds, date, and day directly into the card slots with zero CPU/battery waste.
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
        // Fitted 16:9 canvas container matching the base artwork aspect ratio (1376x768)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(16f / 9f, matchHeightConstraintsFirst = true)
                .graphicsLayer {
                    // AMOLED burn-in protection: micro-orbiting & cyclic luminance breathing
                    translationX = burnInState.shiftX
                    translationY = burnInState.shiftY
                    alpha = burnInState.luminanceFactor
                }
        ) {
            // 1. Base 1:1 High-Resolution Static Artwork
            Image(
                painter = painterResource(id = R.drawable.clock_template_base),
                contentDescription = "Desk Clock Artwork",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )

            // 2. Real-time Live Time & Date Overlay on Canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val w = size.width
                        val h = size.height

                        // Proportional scaling factors based on 1376 x 768 template
                        val largePixelSize = (h / 768f) * 11.2f
                        val smallPixelSize = (h / 768f) * 4.6f
                        val datePixelSize = (h / 768f) * 5.4f

                        // A. LIVE FLIP CLOCK DIGITS (10:09)
                        val cardY = (384f / 768f) * h

                        // Hour Tens (Card 1)
                        val card1X = (442f / 1376f) * w
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
                        val card2X = (582f / 1376f) * w
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
                        val card3X = (758f / 1376f) * w
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
                        val card4X = (898f / 1376f) * w
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
                        val secX = (1014f / 1376f) * w
                        val secY = (448f / 768f) * h
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
                        val dateX = (249f / 1376f) * w
                        val dateY = (163f / 768f) * h
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
                        val dayX = (466f / 1376f) * w
                        val dayY = (163f / 768f) * h
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
                    }
            )
        }
    }
}
