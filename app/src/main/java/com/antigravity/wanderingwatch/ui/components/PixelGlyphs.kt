package com.antigravity.wanderingwatch.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.antigravity.wanderingwatch.ui.theme.PureWhite

/**
 * 100% offline, bulletproof 1-bit pixel art glyph renderer.
 * Renders large clock numerals (7x11) and compact text (5x7) directly on Canvas.
 */
object PixelGlyphs {

    // 7x11 Large Digit Matrix Bitmasks (matching Design v7 flip clock digits)
    private val LARGE_DIGITS = mapOf(
        '0' to arrayOf(
            "0111110",
            "1100011",
            "1100011",
            "1100111",
            "1101111",
            "1111011",
            "1110011",
            "1100011",
            "1100011",
            "1100011",
            "0111110"
        ),
        '1' to arrayOf(
            "0011100",
            "0111100",
            "1101100",
            "0001100",
            "0001100",
            "0001100",
            "0001100",
            "0001100",
            "0001100",
            "0001100",
            "1111111"
        ),
        '2' to arrayOf(
            "0111110",
            "1100011",
            "0000011",
            "0000110",
            "0001100",
            "0011000",
            "0110000",
            "1100000",
            "1100000",
            "1100011",
            "1111111"
        ),
        '3' to arrayOf(
            "0111110",
            "1100011",
            "0000011",
            "0000011",
            "0011110",
            "0000011",
            "0000011",
            "0000011",
            "0000011",
            "1100011",
            "0111110"
        ),
        '4' to arrayOf(
            "0000110",
            "0001110",
            "0011110",
            "0110110",
            "1100110",
            "1111111",
            "0000110",
            "0000110",
            "0000110",
            "0000110",
            "0000110"
        ),
        '5' to arrayOf(
            "1111111",
            "1100000",
            "1100000",
            "1100000",
            "1111110",
            "0000011",
            "0000011",
            "0000011",
            "0000011",
            "1100011",
            "0111110"
        ),
        '6' to arrayOf(
            "0111110",
            "1100011",
            "1100000",
            "1100000",
            "1111110",
            "1100011",
            "1100011",
            "1100011",
            "1100011",
            "1100011",
            "0111110"
        ),
        '7' to arrayOf(
            "1111111",
            "0000011",
            "0000110",
            "0000110",
            "0001100",
            "0001100",
            "0011000",
            "0011000",
            "0110000",
            "0110000",
            "0110000"
        ),
        '8' to arrayOf(
            "0111110",
            "1100011",
            "1100011",
            "1100011",
            "0111110",
            "1100011",
            "1100011",
            "1100011",
            "1100011",
            "1100011",
            "0111110"
        ),
        '9' to arrayOf(
            "0111110",
            "1100011",
            "1100011",
            "1100011",
            "0111111",
            "0000011",
            "0000011",
            "0000011",
            "0000011",
            "1100011",
            "0111110"
        )
    )

    // 5x7 Small Font Matrix Bitmasks (for compact badges: 10/28, FRI, MON, PM, seconds 59)
    private val SMALL_GLYPHS = mapOf(
        '0' to arrayOf("01110", "10001", "10011", "10101", "11001", "10001", "01110"),
        '1' to arrayOf("00100", "01100", "00100", "00100", "00100", "00100", "01110"),
        '2' to arrayOf("01110", "10001", "00001", "00110", "01000", "10000", "11111"),
        '3' to arrayOf("01110", "10001", "00001", "00110", "00001", "10001", "01110"),
        '4' to arrayOf("00010", "00110", "01010", "10010", "11111", "00010", "00010"),
        '5' to arrayOf("11111", "10000", "11110", "00001", "00001", "10001", "01110"),
        '6' to arrayOf("01110", "10001", "10000", "11110", "10001", "10001", "01110"),
        '7' to arrayOf("11111", "00001", "00010", "00100", "01000", "01000", "01000"),
        '8' to arrayOf("01110", "10001", "10001", "01110", "10001", "10001", "01110"),
        '9' to arrayOf("01110", "10001", "10001", "01111", "00001", "10001", "01110"),
        '/' to arrayOf("00001", "00010", "00100", "00100", "01000", "10000", "00000"),
        ':' to arrayOf("00000", "00100", "00000", "00000", "00100", "00000", "00000"),
        'A' to arrayOf("01110", "10001", "10001", "11111", "10001", "10001", "10001"),
        'F' to arrayOf("11111", "10000", "11110", "10000", "10000", "10000", "10000"),
        'M' to arrayOf("10001", "11011", "10101", "10101", "10001", "10001", "10001"),
        'N' to arrayOf("10001", "11001", "10101", "10011", "10001", "10001", "10001"),
        'O' to arrayOf("01110", "10001", "10001", "10001", "10001", "10001", "01110"),
        'P' to arrayOf("11110", "10001", "10001", "11110", "10000", "10000", "10000"),
        'R' to arrayOf("11110", "10001", "10001", "11110", "10100", "10010", "10001"),
        'S' to arrayOf("01111", "10000", "11110", "00001", "00001", "10001", "01110"),
        'T' to arrayOf("11111", "00100", "00100", "00100", "00100", "00100", "00100"),
        'U' to arrayOf("10001", "10001", "10001", "10001", "10001", "10001", "01110"),
        'W' to arrayOf("10001", "10001", "10001", "10101", "10101", "11011", "10001"),
        'E' to arrayOf("11111", "10000", "11110", "10000", "10000", "10000", "11111"),
        'D' to arrayOf("11110", "10001", "10001", "10001", "10001", "10001", "11110"),
        'H' to arrayOf("10001", "10001", "10001", "11111", "10001", "10001", "10001"),
        'I' to arrayOf("01110", "00100", "00100", "00100", "00100", "00100", "01110")
    )

    /**
     * Draws a large 7x11 clock digit centered on the given bounds.
     */
    fun drawLargeDigit(
        drawScope: DrawScope,
        char: Char,
        centerX: Float,
        centerY: Float,
        pixelSize: Float,
        color: Color = PureWhite
    ) {
        val rows = LARGE_DIGITS[char] ?: return
        val totalWidth = 7 * pixelSize
        val totalHeight = 11 * pixelSize
        val startX = centerX - (totalWidth / 2f)
        val startY = centerY - (totalHeight / 2f)

        for (r in rows.indices) {
            val line = rows[r]
            for (c in line.indices) {
                if (line[c] == '1') {
                    drawScope.drawRect(
                        color = color,
                        topLeft = Offset(startX + c * pixelSize, startY + r * pixelSize),
                        size = Size(pixelSize, pixelSize)
                    )
                }
            }
        }
    }

    /**
     * Draws a string of compact 5x7 pixel glyphs.
     */
    fun drawSmallText(
        drawScope: DrawScope,
        text: String,
        startX: Float,
        startY: Float,
        pixelSize: Float,
        spacing: Float = pixelSize,
        color: Color = PureWhite
    ) {
        var cursorX = startX
        for (ch in text) {
            val rows = SMALL_GLYPHS[ch]
            if (rows != null) {
                for (r in rows.indices) {
                    val line = rows[r]
                    for (c in line.indices) {
                        if (line[c] == '1') {
                            drawScope.drawRect(
                                color = color,
                                topLeft = Offset(cursorX + c * pixelSize, startY + r * pixelSize),
                                size = Size(pixelSize, pixelSize)
                            )
                        }
                    }
                }
                cursorX += (5 * pixelSize) + spacing
            } else {
                cursorX += (3 * pixelSize) + spacing
            }
        }
    }

    fun measureSmallTextWidth(text: String, pixelSize: Float, spacing: Float = pixelSize): Float {
        var w = 0f
        for (i in text.indices) {
            w += (5 * pixelSize)
            if (i < text.length - 1) w += spacing
        }
        return w
    }
}
