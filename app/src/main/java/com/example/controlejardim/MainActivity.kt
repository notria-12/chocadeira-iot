package com.example.controlejardim

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.controlejardim.data.IncubatorData
import com.example.controlejardim.ui.screens.IncubatorScreen
import com.example.controlejardim.ui.theme.ControleJardimTheme
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {

    private var client: Mqtt3BlockingClient? = null
    private val brokerHost = "mqtt-dashboard.com"
    private val topic = "projeto_chocadeira/dados"

    // State for Compose UI
    private var incubatorData by mutableStateOf(IncubatorData())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ControleJardimTheme {
                IncubatorScreen(incubatorData = incubatorData)
            }
        }

        // Start MQTT connection in background thread
        Thread {
            connectMqtt()
        }.start()
    }

    private fun connectMqtt() {
        try {
            // Create MQTT client
            client = Mqtt3Client.builder()
                .identifier("android_chocadeira_" + System.currentTimeMillis())
                .serverHost(brokerHost)
                .serverPort(1883)
                .buildBlocking()

            // Connect to broker
            client?.connect()
            Log.d("MQTT", "Connected to broker!")

            // Update connection status
            runOnUiThread {
                incubatorData = incubatorData.copy(isConnected = true)
            }

            // Subscribe to topic and listen for messages
            client?.toAsync()?.subscribeWith()
                ?.topicFilter(topic)
                ?.qos(MqttQos.AT_LEAST_ONCE)
                ?.callback { publish ->
                    val payload = StandardCharsets.UTF_8.decode(publish.payload.get()).toString()
                    Log.d("MQTT", "Message received: $payload")
                    runOnUiThread { updateData(payload) }
                }
                ?.send()

        } catch (e: Exception) {
            Log.e("MQTT", "Error: ${e.message}")
            runOnUiThread {
                incubatorData = incubatorData.copy(isConnected = false)
            }
        }
    }

    private fun updateData(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)

            val temperature = jsonObject.optDouble("temp", 0.0).toFloat()
            val humidity = jsonObject.optDouble("humi", 0.0).toFloat()

            // Determine heater and humidifier status based on values
            // Heater: turns on when temp is below ideal (37.5°C)
            // Humidifier: turns on when humidity is below ideal (55%)
            val isHeaterOn = temperature < IncubatorData.IDEAL_TEMP_MIN
            val isHumidifierOn = humidity < IncubatorData.IDEAL_HUMIDITY_MIN

            incubatorData = incubatorData.copy(
                temperature = temperature,
                humidity = humidity,
                isHeaterOn = isHeaterOn,
                isHumidifierOn = isHumidifierOn,
                isConnected = true
            )

            Log.d("MQTT", "Data updated - Temp: $temperature°C, Humidity: $humidity%")

        } catch (e: Exception) {
            Log.e("App", "JSON parse error: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Thread {
            try {
                client?.disconnect()
                Log.d("MQTT", "Disconnected from broker")
            } catch (e: Exception) {
                Log.e("MQTT", "Error disconnecting: ${e.message}")
            }
        }.start()
    }
}
