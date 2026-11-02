package com.antigravity.wanderingwatch.engine

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

data class BurnInState(
    val shiftX: Float = 0f,
    val shiftY: Float = 0f,
    val luminanceFactor: Float = 1.0f,
    val isNightDimmed: Boolean = false,
    val ambientLux: Float = 50f
)

/**
 * Controller dedicated to protecting the AMOLED screen against burn-in.
 * - Micro-orbits the UI coordinate space by [-3, +3] dp every 60s
 * - Gently breathes peak luminance between 88% and 100% over 5-minute cycles
 * - Auto-dims in low ambient light (< 10 lux)
 */
class BurnInController(
    private val scope: CoroutineScope,
    private val sensorManager: SensorManager?
) : SensorEventListener {

    private val _burnInState = MutableStateFlow(BurnInState())
    val burnInState: StateFlow<BurnInState> = _burnInState.asStateFlow()

    private var lightSensor: Sensor? = null
    private var orbitStep = 0.0

    init {
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        startOrbitAndBreathingLoop()
    }

    fun registerSensors() {
        lightSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun unregisterSensors() {
        sensorManager?.unregisterListener(this)
    }

    private fun startOrbitAndBreathingLoop() {
        scope.launch(Dispatchers.Default) {
            var cycleSeconds = 0
            while (isActive) {
                // Update micro-shift every 60 seconds
                if (cycleSeconds % 60 == 0) {
                    orbitStep += 0.25
                    val newX = (3.0f * sin(orbitStep)).toFloat()
                    val newY = (2.5f * cos(1.4 * orbitStep)).toFloat()

                    _burnInState.update { current ->
                        current.copy(shiftX = newX, shiftY = newY)
                    }
                }

                // Breathing cycle: 300 seconds (5 min) sine wave between 0.88 and 1.00
                val breathSine = sin(2.0 * Math.PI * (cycleSeconds % 300) / 300.0)
                val baseLuminance = (0.94 + 0.06 * breathSine).toFloat()

                _burnInState.update { current ->
                    val effectiveLuminance = if (current.isNightDimmed) {
                        baseLuminance * 0.25f // 75% reduction in pitch-black room
                    } else {
                        baseLuminance
                    }
                    current.copy(luminanceFactor = effectiveLuminance)
                }

                cycleSeconds++
                delay(1000L)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values.getOrNull(0) ?: 50f
            val nightDim = lux < 8.0f // Bedside / dark room threshold
            _burnInState.update { it.copy(ambientLux = lux, isNightDimmed = nightDim) }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
