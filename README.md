# Wandering_Watch 🕰️🐾

A modern, power-efficient AMOLED desk clock application built natively for Android (Google Pixel 9a) using Jetpack Compose and 1-bit retro pixel art.

---

## ✨ Editions Included

1. **Wandering_Watch_Static (1:1 Exact Art Copy)**:
   - Uses the exact high-resolution approved render as the background asset (`clock_template_base.png`).
   - Retains the exact outer halo glow bloom, stippled rolling hills, flowers, stars, celestial moon/clouds, cat, and mechanical flip card hinges.
   - Overlays **live real-time hours, minutes, seconds, date, and day** directly onto the card faces.
   - Virtual 0.0% CPU and battery drain with full AMOLED burn-in protection.

2. **Wandering_Watch (Procedural Animated Canvas)**:
   - 100% vector-procedural Jetpack Compose canvas.
   - Active 8 FPS animated cat walking cycle along the rolling hill curve.
   - Interactive clickable speaker and alarm toggles.

---

## 🛡️ Built-in AMOLED Burn-In Protection
- **Micro-Pixel Orbiting**: Translates root coordinates by $\pm 2\text{--}4\text{ px}$ every 60s in a Lissajous orbit so static borders never sit on the same subpixel.
- **Cyclic Luminance Breathing**: Gently modulates peak brightness between 88% and 100% over 5-minute sine waves.
- **Ambient Auto-Dimming**: Detects low room lighting (< 10 lux) via `Sensor.TYPE_LIGHT` and drops brightness to ~20% with reduced halo glow.

---

## 🚀 How to Open & Run in Android Studio

1. Open **Android Studio**.
2. On the Welcome screen, click **Open** (or go to `File` > `Open...`).
3. Select this folder:
   ```
   c:\Users\manuv\Documents\antigravity\idle-pixel
   ```
4. Click **OK**. Android Studio will automatically perform the initial Gradle Sync.
5. Plug in your **Google Pixel 9a** (with USB Debugging enabled) or start an Android Emulator.
6. Click the green **Run (Play)** button `▶` in the top toolbar to install and launch **Wandering_Watch**!

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin 2.0.21
- **UI Framework:** Jetpack Compose (BOM 2024.11.00)
- **Architecture:** Unidirectional Data Flow (MVI) with Kotlin Coroutines & `StateFlow`
- **Minimum SDK:** Android 8.0 (API 26)
- **Target SDK:** Android 15 (API 35)
- **Typography:** Pixelify Sans (Google Fonts) with Monospace fallback
