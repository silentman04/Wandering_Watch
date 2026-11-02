package com.antigravity.wanderingwatch.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.SensorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.antigravity.wanderingwatch.engine.BurnInController
import com.antigravity.wanderingwatch.engine.BurnInState
import com.antigravity.wanderingwatch.engine.TimeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ClockViewModel(application: Application) : AndroidViewModel(application) {

    private val _timeState = MutableStateFlow(TimeState())
    val timeState: StateFlow<TimeState> = _timeState.asStateFlow()

    private val sensorManager =
        application.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    val burnInController = BurnInController(viewModelScope, sensorManager)
    val burnInState: StateFlow<BurnInState> = burnInController.burnInState

    init {
        burnInController.registerSensors()
        startClockTicker()
    }

    private fun startClockTicker() {
        viewModelScope.launch(Dispatchers.Default) {
            val dayFormat = SimpleDateFormat("EEE", Locale.US)
            while (isActive) {
                val cal = Calendar.getInstance()
                val hour12 = cal.get(Calendar.HOUR).let { if (it == 0) 12 else it }
                val hour24 = cal.get(Calendar.HOUR_OF_DAY)
                val hourStr = String.format(Locale.US, "%02d", hour12)
                val minuteStr = String.format(Locale.US, "%02d", cal.get(Calendar.MINUTE))
                val secondStr = String.format(Locale.US, "%02d", cal.get(Calendar.SECOND))
                val dateStr = String.format(
                    Locale.US,
                    "%02d/%02d",
                    cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                val dayStr = dayFormat.format(cal.time).uppercase(Locale.US)
                val amPmStr = if (cal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
                val isDay = hour24 in 6..17 // 6:00 AM to 5:59 PM

                _timeState.update { current ->
                    current.copy(
                        hourTens = hourStr[0].toString(),
                        hourOnes = hourStr[1].toString(),
                        minuteTens = minuteStr[0].toString(),
                        minuteOnes = minuteStr[1].toString(),
                        seconds = secondStr,
                        date = dateStr,
                        dayOfWeek = dayStr,
                        amPm = amPmStr,
                        isDaytime = isDay
                    )
                }

                // Precision sleep until next wall-clock second boundary
                val nowMillis = System.currentTimeMillis()
                val delayMillis = 1000L - (nowMillis % 1000L)
                delay(delayMillis.coerceAtLeast(50L))
            }
        }
    }

    fun toggleMute() {
        _timeState.update { it.copy(isMuted = !it.isMuted) }
    }

    fun toggleAlarm() {
        _timeState.update { it.copy(isAlarmActive = !it.isAlarmActive) }
    }

    fun setCharging(charging: Boolean) {
        _timeState.update { it.copy(isCharging = charging) }
    }

    override fun onCleared() {
        super.onCleared()
        burnInController.unregisterSensors()
    }
}
