package com.yeidckon.mini_proyecto_integrador.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yeidckon.mini_proyecto_integrador.data.PreferencesManager
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun ListaScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val estudiantes = remember { preferencesManager.getEstudiantes() }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Estudiantes Registrados", style = MaterialTheme.typography.headlineMedium)
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        if (estudiantes.isEmpty()) {
            Text("Aún no hay registros.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(estudiantes) { e ->
                    Text(
                        text = "${e.matricula} — ${e.nombre} (${e.carrera})",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val ruta = "detalle/${Uri.encode(e.matricula)}/${Uri.encode(e.nombre)}/" +
                                        "${Uri.encode(e.carrera)}/${Uri.encode(e.turno)}/${e.activo}"
                                navController.navigate(ruta)
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(
            onClick = { navController.navigate("registro") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Volver a Registro") }
    }
}