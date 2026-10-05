package com.yeidckon.practica05_sharedpreferences

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yeidckon.practica05_sharedpreferences.data.PreferencesManager

@Composable
fun FormScreen() {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    var username by remember { mutableStateOf(preferencesManager.getUsername()) }
    var notificationsEnabled by remember { mutableStateOf(preferencesManager.getNotifications()) }
    var darkThemeEnabled by remember { mutableStateOf(preferencesManager.getDarkTheme()) }
    var showError by remember { mutableStateOf(false) }

    val colorScheme = if (darkThemeEnabled) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Práctica 5: Configuración",
                    style = MaterialTheme.typography.headlineMedium
                )

                HorizontalDivider()

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        showError = false
                    },
                    label = { Text("Nombre de Usuario") },
                    singleLine = true,
                    isError = showError,
                    supportingText = {
                        if (showError) Text("El nombre no puede estar vacío")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Recibir Notificaciones:", fontSize = 16.sp)
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Activar Tema Oscuro:", fontSize = 16.sp)
                    Switch(
                        checked = darkThemeEnabled,
                        onCheckedChange = { darkThemeEnabled = it }
                    )
                }

                HorizontalDivider()

                Button(
                    onClick = {
                        if (username.isBlank()) {
                            showError = true
                            Toast.makeText(context, "Nombre vacío, no se guardó", Toast.LENGTH_SHORT).show()
                        } else {
                            preferencesManager.saveSettings(
                                username.trim(), notificationsEnabled, darkThemeEnabled
                            )
                            Toast.makeText(context, "Configuración guardada", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Guardar Preferencias") }

                OutlinedButton(
                    onClick = {
                        username = preferencesManager.getUsername()
                        notificationsEnabled = preferencesManager.getNotifications()
                        darkThemeEnabled = preferencesManager.getDarkTheme()
                        showError = false
                        Toast.makeText(context, "Preferencias cargadas", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Recargar Datos Guardados") }

                TextButton(
                    onClick = {
                        preferencesManager.clearPreferences()
                        username = ""
                        notificationsEnabled = false
                        darkThemeEnabled = false
                        showError = false
                        Toast.makeText(context, "Preferencias eliminadas", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restablecer Configuración", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}