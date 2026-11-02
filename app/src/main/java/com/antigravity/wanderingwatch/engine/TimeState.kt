package com.antigravity.wanderingwatch.engine

data class TimeState(
    val hourTens: String = "1",
    val hourOnes: String = "0",
    val minuteTens: String = "0",
    val minuteOnes: String = "9",
    val seconds: String = "59",
    val date: String = "10/28",
    val dayOfWeek: String = "FRI",
    val amPm: String = "PM",
    val isMuted: Boolean = false,
    val isAlarmActive: Boolean = true,
    val isCharging: Boolean = true
)
