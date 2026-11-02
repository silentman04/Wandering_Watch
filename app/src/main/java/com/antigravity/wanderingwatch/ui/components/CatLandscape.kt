package com.antigravity.wanderingwatch.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.antigravity.wanderingwatch.ui.theme.PureWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.sin

/**
 * Continuous panoramic landscape across the full bottom of the screen.
 * Displays delicate rolling grassy hills, tiny pixel flowers, and an 8-bit white cat
 * walking across the horizon in an authentic low-power 8 FPS retro sprite loop.
 */
@Composable
fun CatLandscape(
    modifier: Modifier = Modifier
) {
    // 8 FPS Sprite walk-cycle frame (4 frames: 0, 1, 2, 3)
    var spriteFrame by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(125L) // 8 FPS
            spriteFrame = (spriteFrame + 1) % 4
        }
    }

    // Smooth horizontal walking progress of the cat across the screen
    val infiniteTransition = rememberInfiniteTransition(label = "CatWalk")
    val catProgress by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.92f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 32000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CatWalkProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .drawBehind {
                val p = 2.dp.toPx()
                val w = size.width
                val h = size.height

                // 1. Background Hill Line (subtle gentle rolling curve)
                val bgHillPath = Path().apply {
                    moveTo(0f, h * 0.55f)
                    cubicTo(
                        w * 0.25f, h * 0.40f,
                        w * 0.50f, h * 0.65f,
                        w * 0.75f, h * 0.45f
                    )
                    cubicTo(
                        w * 0.88f, h * 0.35f,
                        w * 0.95f, h * 0.50f,
                        w, h * 0.45f
                    )
                }
                drawPath(
                    path = bgHillPath,
                    color = PureWhite.copy(alpha = 0.35f),
                    style = Stroke(width = 1.dp.toPx())
                )

                // 2. Foreground Rolling Hill Line (where cat walks)
                fun getHillY(xFrac: Float): Float {
                    val base = h * 0.62f
                    val wave1 = (sin(xFrac * Math.PI * 2.2) * (h * 0.16f)).toFloat()
                    val wave2 = (sin(xFrac * Math.PI * 4.4 + 1.0) * (h * 0.06f)).toFloat()
                    return base + wave1 + wave2
                }

                val fgHillPath = Path().apply {
                    moveTo(0f, getHillY(0f))
                    var step = 0.02f
                    while (step <= 1.0f) {
                        lineTo(step * w, getHillY(step))
                        step += 0.02f
                    }
                }
                drawPath(
                    path = fgHillPath,
                    color = PureWhite,
                    style = Stroke(width = 1.5.dp.toPx())
                )

                // 3. Delicate grass tufts and tiny pixel flowers scattered along the hill
                val floraPoints = listOf(0.08f, 0.18f, 0.28f, 0.42f, 0.58f, 0.72f, 0.85f, 0.94f)
                for (fx in floraPoints) {
                    val flowerX = fx * w
                    val flowerY = getHillY(fx)

                    // Tiny grass blade
                    drawRect(PureWhite, Offset(flowerX, flowerY - 4 * p), Size(p, 4 * p))
                    drawRect(PureWhite, Offset(flowerX - p, flowerY - 3 * p), Size(p, 2 * p))
                    drawRect(PureWhite, Offset(flowerX + p, flowerY - 2 * p), Size(p, 2 * p))

                    // Small pixel flower head on some tufts
                    if (fx == 0.18f || fx == 0.58f || fx == 0.94f) {
                        drawRect(PureWhite, Offset(flowerX - p, flowerY - 6 * p), Size(3 * p, 2 * p))
                        drawRect(PureWhite, Offset(flowerX, flowerY - 7 * p), Size(p, p))
                    }
                }

                // 4. Starlit sparkles in the lower sky
                val starPoints = listOf(
                    Pair(0.12f, h * 0.25f),
                    Pair(0.35f, h * 0.30f),
                    Pair(0.65f, h * 0.22f),
                    Pair(0.82f, h * 0.28f)
                )
                for ((sx, sy) in starPoints) {
                    drawRect(PureWhite.copy(alpha = 0.8f), Offset(sx * w, sy), Size(p, p))
                }

                // 5. Draw the 8-Bit White Cat Mascot walking along the hill
                val catX = catProgress * w
                val catY = getHillY(catProgress)
                draw8BitCat(catX, catY, p, spriteFrame)
            }
    )
}

/**
 * Draws an adorable 8-bit white cat sprite with active 4-frame walk cycle.
 */
private fun DrawScope.draw8BitCat(
    footX: Float,
    footY: Float,
    p: Float,
    frame: Int
) {
    val originX = footX - 12 * p
    val originY = footY - 18 * p

    fun px(rx: Int, ry: Int, w: Int = 1, h: Int = 1) {
        drawRect(PureWhite, Offset(originX + rx * p, originY + ry * p), Size(w * p, h * p))
    }

    // Cat Body (Solid 8-bit white block)
    px(6, 6, 11, 7)

    // Cat Head
    px(14, 3, 7, 6)

    // Cat Ears
    px(15, 1, 2, 2)
    px(19, 1, 2, 2)

    // Little black pixel eye and nose
    drawRect(Color.Black, Offset(originX + 18 * p, originY + 5 * p), Size(p, p))
    drawRect(Color.Black, Offset(originX + 20 * p, originY + 6 * p), Size(p, p))

    // Animated Tail (swishes gently with walk frames)
    val tailCurve = when (frame) {
        0 -> 0
        1 -> -1
        2 -> -2
        else -> -1
    }
    px(4 + tailCurve, 4, 2, 2)
    px(3 + tailCurve, 2, 2, 3)
    px(2 + tailCurve, 1, 2, 2)

    // Animated Walking Legs (4-frame cycle)
    when (frame) {
        0 -> {
            // Front right forward, back right forward
            px(7, 13, 2, 4)
            px(11, 13, 2, 4)
            px(14, 13, 2, 5)
            px(18, 13, 2, 3)
        }
        1 -> {
            // Passing frame
            px(8, 13, 2, 4)
            px(10, 13, 2, 4)
            px(15, 13, 2, 4)
            px(17, 13, 2, 4)
        }
        2 -> {
            // Front left forward, back left forward
            px(9, 13, 2, 3)
            px(12, 13, 2, 5)
            px(16, 13, 2, 4)
            px(17, 13, 2, 4)
        }
        else -> {
            // Passing frame
            px(8, 13, 2, 4)
            px(11, 13, 2, 4)
            px(15, 13, 2, 4)
            px(17, 13, 2, 4)
        }
    }
}
