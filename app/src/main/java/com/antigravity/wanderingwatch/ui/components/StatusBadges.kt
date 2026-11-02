package com.antigravity.wanderingwatch.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antigravity.wanderingwatch.ui.theme.DarkFlipCardBg
import com.antigravity.wanderingwatch.ui.theme.FlipCardBorder
import com.antigravity.wanderingwatch.ui.theme.PixelFontFamily
import com.antigravity.wanderingwatch.ui.theme.PureWhite

/**
 * Top-left compact date ("10/28") and day of week ("FRI") outline badges.
 */
@Composable
fun DateDayBadges(
    date: String,
    dayOfWeek: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date Badge (e.g. "10/28")
        Box(
            modifier = Modifier
                .background(DarkFlipCardBg, shape = RoundedCornerShape(6.dp))
                .border(1.5.dp, PureWhite, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date,
                fontFamily = PixelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = PureWhite
            )
        }

        // Day Badge (e.g. "FRI")
        Box(
            modifier = Modifier
                .background(DarkFlipCardBg, shape = RoundedCornerShape(6.dp))
                .border(1.5.dp, PureWhite, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfWeek,
                fontFamily = PixelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = PureWhite
            )
        }
    }
}

/**
 * Vertical status stack on the left: Speaker, Alarm Bell, AM/PM Badge.
 */
@Composable
fun LeftStatusStack(
    amPm: String,
    isMuted: Boolean,
    isAlarmActive: Boolean,
    onToggleMute: () -> Unit,
    onToggleAlarm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pixel Speaker Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggleMute
                )
                .drawBehind {
                    val p = 2.dp.toPx()
                    // Speaker body
                    drawRect(color = PureWhite, topLeft = Offset(4 * p, 4 * p), size = Size(3 * p, 8 * p))
                    // Cone
                    val conePath = Path().apply {
                        moveTo(7 * p, 4 * p)
                        lineTo(11 * p, 1 * p)
                        lineTo(11 * p, 15 * p)
                        lineTo(7 * p, 12 * p)
                        close()
                    }
                    drawPath(conePath, color = PureWhite)

                    if (!isMuted) {
                        // Sound waves
                        drawArc(
                            color = PureWhite,
                            startAngle = -45f,
                            sweepAngle = 90f,
                            useCenter = false,
                            topLeft = Offset(8 * p, 2 * p),
                            size = Size(8 * p, 12 * p),
                            style = Stroke(width = 1.5.dp.toPx())
                        )
                    } else {
                        // Mute diagonal line
                        drawLine(
                            color = PureWhite,
                            start = Offset(2 * p, 14 * p),
                            end = Offset(14 * p, 2 * p),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
                }
        )

        // Pixel Alarm Bell Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onToggleAlarm
                )
                .drawBehind {
                    val p = 2.dp.toPx()
                    val alpha = if (isAlarmActive) 1.0f else 0.35f

                    // Top loop
                    drawRect(color = PureWhite.copy(alpha = alpha), topLeft = Offset(7 * p, 1 * p), size = Size(2 * p, 2 * p))

                    // Bell body
                    val bellPath = Path().apply {
                        moveTo(8 * p, 3 * p)
                        lineTo(12 * p, 10 * p)
                        lineTo(14 * p, 12 * p)
                        lineTo(2 * p, 12 * p)
                        lineTo(4 * p, 10 * p)
                        close()
                    }
                    drawPath(bellPath, color = PureWhite.copy(alpha = alpha))

                    // Clapper
                    drawRect(color = PureWhite.copy(alpha = alpha), topLeft = Offset(7 * p, 13 * p), size = Size(2 * p, 2 * p))
                }
        )

        // AM/PM Badge
        Box(
            modifier = Modifier
                .width(42.dp)
                .height(30.dp)
                .background(DarkFlipCardBg, shape = RoundedCornerShape(6.dp))
                .border(1.5.dp, PureWhite, shape = RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = amPm,
                fontFamily = PixelFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = PureWhite
            )
        }
    }
}
