package com.yeidckon.mini_proyecto_integrador.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetalleScreen(
    matricula: String,
    nombre: String,
    carrera: String,
    turno: String,
    activo: Boolean,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Confirmación de Registro", style = MaterialTheme.typography.headlineMedium)
        HorizontalDivider()

        Text("Matrícula: $matricula", style = MaterialTheme.typography.bodyLarge)
        Text("Nombre: $nombre", style = MaterialTheme.typography.bodyLarge)
        Text("Carrera: $carrera", style = MaterialTheme.typography.bodyLarge)
        Text("Turno: $turno", style = MaterialTheme.typography.bodyLarge)
        Text(
            "Estatus: ${if (activo) "Activo" else "Inactivo"}",
            color = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )

        HorizontalDivider()

        Button(
            onClick = { navController.navigate("registro") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Nuevo Registro") }
    }
}