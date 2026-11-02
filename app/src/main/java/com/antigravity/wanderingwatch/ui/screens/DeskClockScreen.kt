package com.antigravity.wanderingwatch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.antigravity.wanderingwatch.ui.components.CatLandscape
import com.antigravity.wanderingwatch.ui.components.DateDayBadges
import com.antigravity.wanderingwatch.ui.components.DockedSecondsBadge
import com.antigravity.wanderingwatch.ui.components.FlipCardDigit
import com.antigravity.wanderingwatch.ui.components.FlipColonSeparator
import com.antigravity.wanderingwatch.ui.components.LeftStatusStack
import com.antigravity.wanderingwatch.ui.components.OuterHaloContainer
import com.antigravity.wanderingwatch.ui.components.WeatherWidget
import com.antigravity.wanderingwatch.ui.theme.PureBlack
import com.antigravity.wanderingwatch.viewmodel.ClockViewModel

/**
 * Master screen layout for Wandering_Watch.
 * Assembles all widgets inside the BurnIn protection layer and outer halo.
 */
@Composable
fun DeskClockScreen(
    viewModel: ClockViewModel,
    modifier: Modifier = Modifier
) {
    val timeState by viewModel.timeState.collectAsState()
    val burnInState by viewModel.burnInState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // Outer ambient glow ring
        OuterHaloContainer(
            isNightDimmed = burnInState.isNightDimmed
        ) {
            // AMOLED Burn-in protection wrapper: applies micro-orbiting (shiftX, shiftY)
            // and cyclic luminance breathing (luminanceFactor)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationX = burnInState.shiftX
                        translationY = burnInState.shiftY
                        alpha = burnInState.luminanceFactor
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // TOP ROW: Date & Day Badges (Left) and Weather Widget (Right)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        DateDayBadges(
                            date = timeState.date,
                            dayOfWeek = timeState.dayOfWeek
                        )

                        WeatherWidget()
                    }

                    // CENTER ROW: Status Stack (Left) + Flip Clock Cards & Docked Seconds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Left Status Stack (Speaker, Bell, PM)
                        LeftStatusStack(
                            amPm = timeState.amPm,
                            isMuted = timeState.isMuted,
                            isAlarmActive = timeState.isAlarmActive,
                            onToggleMute = { viewModel.toggleMute() },
                            onToggleAlarm = { viewModel.toggleAlarm() },
                            modifier = Modifier.padding(end = 24.dp)
                        )

                        // Flip Cards Group
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Hour Digits
                            FlipCardDigit(digit = timeState.hourTens)
                            FlipCardDigit(digit = timeState.hourOnes)

                            // Colon separator
                            FlipColonSeparator()

                            // Minute Digits
                            FlipCardDigit(digit = timeState.minuteTens)

                            // Minute Ones + Docked Seconds Badge
                            Box(
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                FlipCardDigit(digit = timeState.minuteOnes)

                                // Docked seconds badge at lower right corner
                                DockedSecondsBadge(
                                    seconds = timeState.seconds,
                                    modifier = Modifier
                                        .offset(x = 58.dp, y = (-2).dp)
                                )
                            }

                            // Spacer to balance the offset of the seconds badge
                            Spacer(modifier = Modifier.padding(end = 56.dp))
                        }
                    }

                    // BOTTOM ROW: Continuous panoramic rolling hill landscape with 8-bit cat
                    CatLandscape(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
