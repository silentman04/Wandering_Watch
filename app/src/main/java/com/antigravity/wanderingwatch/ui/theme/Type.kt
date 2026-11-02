package com.antigravity.wanderingwatch.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.antigravity.wanderingwatch.R

// Google Fonts provider for downloadable fonts
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val pixelifySansFont = GoogleFont("Pixelify Sans")

val PixelFontFamily = FontFamily(
    Font(
        googleFont = pixelifySansFont,
        fontProvider = provider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = pixelifySansFont,
        fontProvider = provider,
        weight = FontWeight.Bold
    )
)

// Monospace fallback for instant offline rendering
val FallbackPixelFont = FontFamily.Monospace

val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = PixelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        letterSpacing = 2.sp,
        color = PureWhite
    ),
    headlineMedium = TextStyle(
        fontFamily = PixelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = 1.sp,
        color = PureWhite
    ),
    titleMedium = TextStyle(
        fontFamily = PixelFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 1.sp,
        color = PureWhite
    ),
    bodyMedium = TextStyle(
        fontFamily = PixelFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = PureWhite
    )
)
