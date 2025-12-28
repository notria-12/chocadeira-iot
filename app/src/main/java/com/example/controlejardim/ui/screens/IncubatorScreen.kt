package com.example.controlejardim.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Egg
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.controlejardim.data.IncubatorData
import com.example.controlejardim.ui.components.ConnectionStatusBar
import com.example.controlejardim.ui.components.DataCard
import com.example.controlejardim.ui.components.StatusIndicatorCard
import com.example.controlejardim.ui.theme.HeaterOff
import com.example.controlejardim.ui.theme.HeaterOn
import com.example.controlejardim.ui.theme.HumidifierOff
import com.example.controlejardim.ui.theme.HumidifierOn
import com.example.controlejardim.ui.theme.HumidityOk
import com.example.controlejardim.ui.theme.TemperatureWarm
import kotlinx.coroutines.delay

@Composable
fun IncubatorScreen(
    incubatorData: IncubatorData,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Connection status bar
            ConnectionStatusBar(isConnected = incubatorData.isConnected)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { -40 }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(vertical = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Egg,
                                contentDescription = "Egg",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = "  Chocadeira IoT",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Monitoramento em tempo real",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Temperature Card
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { 60 }
                ) {
                    DataCard(
                        title = "Temperatura",
                        value = String.format("%.1f", incubatorData.temperature),
                        unit = "°C",
                        icon = Icons.Rounded.Thermostat,
                        iconTint = getTemperatureColor(incubatorData.temperature),
                        gradientColors = listOf(
                            Color(0xFFFF5722),
                            Color(0xFFFF9800),
                            Color(0xFFFFEB3B)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Humidity Card
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { 100 }
                ) {
                    DataCard(
                        title = "Umidade",
                        value = String.format("%.1f", incubatorData.humidity),
                        unit = "%",
                        icon = Icons.Rounded.WaterDrop,
                        iconTint = getHumidityColor(incubatorData.humidity),
                        gradientColors = listOf(
                            Color(0xFF03A9F4),
                            Color(0xFF00BCD4),
                            Color(0xFF26C6DA)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Status Section Title
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { 140 }
                ) {
                    Text(
                        text = "Status dos Dispositivos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Heater Status
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { 180 }
                ) {
                    StatusIndicatorCard(
                        title = "Aquecedor",
                        isActive = incubatorData.isHeaterOn,
                        icon = Icons.Rounded.LocalFireDepartment,
                        activeColor = HeaterOn,
                        inactiveColor = HeaterOff,
                        activeText = "AQUECENDO",
                        inactiveText = "DESLIGADO"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Humidifier Status
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn() + slideInVertically { 220 }
                ) {
                    StatusIndicatorCard(
                        title = "Umidificador",
                        isActive = incubatorData.isHumidifierOn,
                        icon = Icons.Rounded.Waves,
                        activeColor = HumidifierOn,
                        inactiveColor = HumidifierOff,
                        activeText = "UMIDIFICANDO",
                        inactiveText = "DESLIGADO"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Info footer
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🥚 Temperatura ideal: 37.5°C - 38.0°C",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💧 Umidade ideal: 55% - 65%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun getTemperatureColor(temperature: Float): Color {
    return when {
        temperature < 35f -> Color(0xFF42A5F5)      // Cold - Blue
        temperature < 37.5f -> Color(0xFFFFB74D)    // Cool - Yellow
        temperature <= 38.0f -> Color(0xFF66BB6A)   // Ideal - Green
        temperature <= 40f -> Color(0xFFFF7043)     // Warm - Orange
        else -> Color(0xFFE53935)                   // Hot - Red
    }
}

@Composable
private fun getHumidityColor(humidity: Float): Color {
    return when {
        humidity < 40f -> Color(0xFFFFB74D)         // Low - Yellow
        humidity < 55f -> Color(0xFF29B6F6)         // Below ideal - Light blue
        humidity <= 65f -> Color(0xFF66BB6A)        // Ideal - Green
        humidity <= 80f -> Color(0xFF26C6DA)        // Above ideal - Cyan
        else -> Color(0xFF5C6BC0)                   // Too high - Indigo
    }
}

