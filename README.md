# Wandering_Watch 🕰️🐾

A modern, power-efficient AMOLED desk clock application built natively for Android (Google Pixel 9a) using Jetpack Compose and 1-bit retro pixel art.

---

## ✨ Features

- **AMOLED Pitch Black Canvas (`#000000`)**: Maximizes battery life and contrast on Pixel 9a Actua OLED displays.
- **Built-in AMOLED Burn-In Protection**:
  - **Micro-Pixel Orbiting**: Translates root coordinates by $\pm 2\text{--}4\text{ px}$ every 60s in a Lissajous orbit so static borders never sit on the same subpixel.
  - **Cyclic Luminance Breathing**: Gently modulates peak brightness between 88% and 100% over 5-minute sine waves.
  - **Ambient Auto-Dimming**: Detects low room lighting (< 10 lux) via `Sensor.TYPE_LIGHT` and drops brightness to ~20% with reduced halo glow.
- **Segmented Flip Clock (`10:09`)**: High-contrast dark cards with central horizontal seams and mechanical hinge tabs.
- **Docked Seconds Badge (`59`)**: Docked neatly at the lower-right corner of the minute card without colon clutter.
- **Top-Left Badges**: Compact date (`10/28`) and day of week (`FRI`) outline pills.
- **Interactive Left Status Stack**:
  - Pixel Audio/Speaker (tap to toggle mute)
  - Pixel Alarm Bell (tap to toggle alarm active state)
  - AM/PM Indicator
- **Top-Right Weather Widget**: Crescent moon resting on fluffy pixel clouds with twinkling stars and subtle ambient drift.
- **Panoramic Cat Horizon**: Full-width rolling grassy hills with flowers and an 8-bit white cat walking in an ultra-low-power 8 FPS sprite animation loop.
- **Pixel 9a Sleek Outer Halo**: White ambient neon glowing rim matching modern phone bezel curvature.

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
