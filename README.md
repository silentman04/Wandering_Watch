# Wandering_Watch 🕰️🐾

A modern, power-efficient AMOLED desk clock application built natively for Android (Google Pixel 9a) using Jetpack Compose and retro 1-bit monochrome pixel art.

---

## ✨ Features & Editions

1. **Wandering_Watch_Static (1:1 Exact Art Copy)**:
   - Built directly on the approved high-resolution artwork (`1272 × 660 px`).
   - Retains the exact outer halo glow bloom, stippled rolling hills, flowers, stars, celestial weather widget, cat mascot, and mechanical flip card hinges.
   - Overlays **live real-time hours, minutes, seconds, date, day, and AM/PM pill** directly onto the card faces.
   - **Automatic Day / Night Celestial Transitions**: Dynamically switches between the night-time crescent moon/stars and the daytime radiant sun/clouds based on real-time solar hours (6 AM to 6 PM).
   - Near 0.0% CPU and battery drain with full AMOLED burn-in protection.

2. **Wandering_Watch (Procedural Animated Canvas)**:
   - 100% vector-procedural Jetpack Compose canvas.
   - Active 8 FPS animated cat walking cycle along the rolling hill curve.
   - Interactive clickable speaker and alarm toggles.

3. **Modular Slices & Interactive PowerPoint Grid Layout**:
   - Includes 7 isolated, transparent 32-bit PNG slices in [`slices/`](slices/) for every UI zone.
   - Includes [`Wandering_Watch_Grid_Layout.pptx`](Wandering_Watch_Grid_Layout.pptx) with named layers in the Selection Pane (`Alt + F10`) for visual drag-and-drop repositioning.

---

## 🛡️ Built-in AMOLED Burn-In Protection
- **Micro-Pixel Orbiting**: Translates root coordinates by $\pm 2\text{--}4\text{ px}$ every 60s in a Lissajous orbit so static borders never sit on the same subpixel.
- **Cyclic Luminance Breathing**: Gently modulates peak brightness between 88% and 100% over 5-minute sine waves.
- **Ambient Auto-Dimming**: Detects low room lighting (< 10 lux) via `Sensor.TYPE_LIGHT` and drops brightness to ~20% with reduced halo glow.

---

## 🚀 How to Open & Run in Android Studio

1. Open **Android Studio**.
2. On the Welcome screen, click **Open** (or go to `File` > `Open...`).
3. Select this repository directory.
4. Android Studio will automatically perform the initial Gradle Sync.
5. Plug in your **Google Pixel 9a** (with USB Debugging enabled) or start an Android Emulator.
6. Click the green **Run (Play)** button `▶` in the top toolbar to install and launch **Wandering_Watch**!

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin 2.0.21
- **UI Framework:** Jetpack Compose (BOM 2024.11.00)
- **Architecture:** Unidirectional Data Flow (MVI) with Kotlin Coroutines & `StateFlow`
- **Target Platform:** Android 15 (API 35), Google Pixel 9a Landscape Display
- **Minimum SDK:** Android 8.0 (API 26)
- **Typography:** Pixelify Sans (Google Fonts) with Monospace fallback
