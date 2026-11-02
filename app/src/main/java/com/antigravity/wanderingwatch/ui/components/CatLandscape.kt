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
 * Displays rolling hills, flora/flowers, and a prominently sized 8-bit white cat
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
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 36000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "CatWalkProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(105.dp)
            .drawBehind {
                val p = 2.4.dp.toPx()
                val w = size.width
                val h = size.height

                // 1. Background Hill Line (subtle gentle rolling curve)
                val bgHillPath = Path().apply {
                    moveTo(0f, h * 0.45f)
                    cubicTo(
                        w * 0.22f, h * 0.30f,
                        w * 0.48f, h * 0.58f,
                        w * 0.72f, h * 0.38f
                    )
                    cubicTo(
                        w * 0.86f, h * 0.28f,
                        w * 0.94f, h * 0.42f,
                        w, h * 0.36f
                    )
                }
                drawPath(
                    path = bgHillPath,
                    color = PureWhite.copy(alpha = 0.35f),
                    style = Stroke(width = 1.2.dp.toPx())
                )

                // 2. Foreground Rolling Hill Line (where cat walks)
                fun getHillY(xFrac: Float): Float {
                    val base = h * 0.65f
                    val wave1 = (sin(xFrac * Math.PI * 2.2) * (h * 0.16f)).toFloat()
                    val wave2 = (sin(xFrac * Math.PI * 4.4 + 1.2) * (h * 0.06f)).toFloat()
                    return base + wave1 + wave2
                }

                val fgHillPath = Path().apply {
                    moveTo(0f, getHillY(0f))
                    var step = 0.015f
                    while (step <= 1.0f) {
                        lineTo(step * w, getHillY(step))
                        step += 0.015f
                    }
                }
                drawPath(
                    path = fgHillPath,
                    color = PureWhite,
                    style = Stroke(width = 2.dp.toPx())
                )

                // 3. Pixel grass tufts and flowers scattered along the path
                val floraPoints = listOf(0.06f, 0.15f, 0.26f, 0.40f, 0.54f, 0.68f, 0.82f, 0.94f)
                for (fx in floraPoints) {
                    val flowerX = fx * w
                    val flowerY = getHillY(fx)

                    // Stepped grass blades
                    drawRect(PureWhite, Offset(flowerX, flowerY - 5 * p), Size(p, 5 * p))
                    drawRect(PureWhite, Offset(flowerX - p, flowerY - 3 * p), Size(p, 3 * p))
                    drawRect(PureWhite, Offset(flowerX + p, flowerY - 4 * p), Size(p, 4 * p))

                    // Pixel flowers on selected hills
                    if (fx == 0.15f || fx == 0.54f || fx == 0.82f || fx == 0.94f) {
                        drawRect(PureWhite, Offset(flowerX - p, flowerY - 8 * p), Size(3 * p, 2 * p))
                        drawRect(PureWhite, Offset(flowerX, flowerY - 9 * p), Size(p, p))
                        drawRect(PureWhite, Offset(flowerX, flowerY - 6 * p), Size(p, p))
                    }
                }

                // 4. Starlit sparkles in the lower sky
                val starPoints = listOf(
                    Pair(0.08f, h * 0.18f),
                    Pair(0.30f, h * 0.22f),
                    Pair(0.55f, h * 0.16f),
                    Pair(0.78f, h * 0.24f),
                    Pair(0.92f, h * 0.14f)
                )
                for ((sx, sy) in starPoints) {
                    drawRect(PureWhite.copy(alpha = 0.85f), Offset(sx * w, sy), Size(1.5f * p, 1.5f * p))
                }

                // 5. Draw the 8-Bit White Cat Mascot (2.2x larger scale)
                val catX = catProgress * w
                val catY = getHillY(catProgress)
                drawEnlarged8BitCat(catX, catY, p * 1.35f, spriteFrame)
            }
    )
}

/**
 * Draws a prominently sized 8-bit white cat sprite with authentic 4-frame walk cycle.
 */
private fun DrawScope.drawEnlarged8BitCat(
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

    // Cat Solid White Body
    px(6, 6, 12, 8)

    // Cat Head
    px(15, 3, 8, 7)

    // Cat Pointed Ears
    px(16, 0, 2, 3)
    px(20, 0, 2, 3)

    // Cat Muzzle
    px(21, 6, 2, 4)

    // Eye and Nose (Black pixels)
    drawRect(Color.Black, Offset(originX + 19 * p, originY + 5 * p), Size(1.2f * p, 1.2f * p))
    drawRect(Color.Black, Offset(originX + 22 * p, originY + 7 * p), Size(1.2f * p, 1.2f * p))

    // Animated Tail (swishes gently)
    val tailOffset = when (frame) {
        0 -> 0
        1 -> -1
        2 -> -2
        else -> -1
    }
    px(4 + tailOffset, 5, 2, 2)
    px(3 + tailOffset, 3, 2, 3)
    px(2 + tailOffset, 1, 2, 3)
    px(3 + tailOffset, 0, 2, 2)

    // Animated Walking Legs (4-frame walk cycle)
    when (frame) {
        0 -> {
            // Right front forward, left back forward
            px(7, 14, 2, 4)
            px(11, 14, 2, 5)
            px(15, 14, 2, 5)
            px(19, 14, 2, 3)
        }
        1 -> {
            // Passing frame
            px(8, 14, 2, 4)
            px(10, 14, 2, 4)
            px(16, 14, 2, 4)
            px(18, 14, 2, 4)
        }
        2 -> {
            // Left front forward, right back forward
            px(9, 14, 2, 3)
            px(12, 14, 2, 5)
            px(17, 14, 2, 4)
            px(18, 14, 2, 4)
        }
        else -> {
            // Passing frame
            px(8, 14, 2, 4)
            px(11, 14, 2, 4)
            px(16, 14, 2, 4)
            px(18, 14, 2, 4)
        }
    }
}
