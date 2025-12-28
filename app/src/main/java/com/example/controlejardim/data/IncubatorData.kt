package com.example.controlejardim.data

/**
 * Represents the current state of the egg incubator.
 *
 * @property temperature Current temperature in Celsius
 * @property humidity Current humidity percentage
 * @property isHeaterOn Whether the heater is currently active
 * @property isHumidifierOn Whether the humidifier is currently active
 * @property isConnected Whether the app is connected to the MQTT broker
 * @property currentDay Current day of incubation (days since start)
 * @property daysRemaining Days remaining until hatching
 */
data class IncubatorData(
    val temperature: Float = 0f,
    val humidity: Float = 0f,
    val isHeaterOn: Boolean = false,
    val isHumidifierOn: Boolean = false,
    val isConnected: Boolean = false,
    val currentDay: Int = 0,
    val daysRemaining: Int = 0
) {
    companion object {
        // Ideal incubation parameters for chicken eggs
        const val IDEAL_TEMP_MIN = 37.5f
        const val IDEAL_TEMP_MAX = 38.0f
        const val IDEAL_HUMIDITY_MIN = 55f
        const val IDEAL_HUMIDITY_MAX = 65f

        // Alert thresholds
        const val TEMP_CRITICAL_LOW = 35f
        const val TEMP_CRITICAL_HIGH = 40f
        const val HUMIDITY_CRITICAL_LOW = 40f
        const val HUMIDITY_CRITICAL_HIGH = 80f

        // Total incubation period for chicken eggs (typically 21 days)
        const val TOTAL_INCUBATION_DAYS = 21
    }

    /** Total days of incubation cycle */
    val totalDays: Int
        get() = currentDay + daysRemaining

    /** Progress percentage (0.0 to 1.0) */
    val progress: Float
        get() = if (totalDays > 0) currentDay.toFloat() / totalDays.toFloat() else 0f

    val temperatureStatus: TemperatureStatus
        get() = when {
            temperature < TEMP_CRITICAL_LOW -> TemperatureStatus.CRITICAL_LOW
            temperature < IDEAL_TEMP_MIN -> TemperatureStatus.LOW
            temperature <= IDEAL_TEMP_MAX -> TemperatureStatus.IDEAL
            temperature <= TEMP_CRITICAL_HIGH -> TemperatureStatus.HIGH
            else -> TemperatureStatus.CRITICAL_HIGH
        }

    val humidityStatus: HumidityStatus
        get() = when {
            humidity < HUMIDITY_CRITICAL_LOW -> HumidityStatus.CRITICAL_LOW
            humidity < IDEAL_HUMIDITY_MIN -> HumidityStatus.LOW
            humidity <= IDEAL_HUMIDITY_MAX -> HumidityStatus.IDEAL
            humidity <= HUMIDITY_CRITICAL_HIGH -> HumidityStatus.HIGH
            else -> HumidityStatus.CRITICAL_HIGH
        }
}

enum class TemperatureStatus {
    CRITICAL_LOW,
    LOW,
    IDEAL,
    HIGH,
    CRITICAL_HIGH
}

enum class HumidityStatus {
    CRITICAL_LOW,
    LOW,
    IDEAL,
    HIGH,
    CRITICAL_HIGH
}
