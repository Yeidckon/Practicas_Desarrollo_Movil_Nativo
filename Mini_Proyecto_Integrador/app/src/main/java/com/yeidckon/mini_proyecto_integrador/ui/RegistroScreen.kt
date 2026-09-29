package com.yeidckon.mini_proyecto_integrador.ui

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yeidckon.mini_proyecto_integrador.data.PreferencesManager
import com.yeidckon.mini_proyecto_integrador.model.Estudiante

private val carreras = listOf(
    "Ingeniería en Software",
    "Ingeniería Civil",
    "Ingeniería en Procesos Industriales",
    "Ingeniería en Geodesia"
)
private val matriculaRegex = Regex("^\\d{7}-\\d\$")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val ultimo = remember { preferencesManager.getLastRegistro() }

    var matricula by remember { mutableStateOf(ultimo.matricula) }
    var nombre by remember { mutableStateOf(ultimo.nombre) }
    var carreraSeleccionada by remember { mutableStateOf(ultimo.carrera.ifBlank { carreras[0] }) }
    var turno by remember { mutableStateOf(ultimo.turno) }
    var activo by remember { mutableStateOf(ultimo.activo) }

    var expanded by remember { mutableStateOf(false) }
    var errorMatricula by remember { mutableStateOf(false) }
    var errorNombre by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Registro de Estudiante", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = matricula,
            onValueChange = {
                matricula = it
                errorMatricula = false
            },
            label = { Text("Matrícula (#######-#)") },
            singleLine = true,
            isError = errorMatricula,
            supportingText = { if (errorMatricula) Text("Formato inválido, ej: 1234567-8") },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = androidx.compose.ui.graphics.Color.Black,
                fontSize = 18.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = false
            },
            label = { Text("Nombre completo") },
            singleLine = true,
            isError = errorNombre,
            supportingText = { if (errorNombre) Text("El nombre no puede estar vacío") },
            textStyle = androidx.compose.ui.text.TextStyle(
                color = androidx.compose.ui.graphics.Color.Black,
                fontSize = 18.sp
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown de carrera
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = carreraSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Carrera") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = androidx.compose.ui.graphics.Color.Black,
                    fontSize = 18.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                carreras.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            carreraSeleccionada = opcion
                            expanded = false
                        }
                    )
                }
            }
        }

        // RadioButtons de turno
        Text("Turno:", style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = turno == "Matutino", onClick = { turno = "Matutino" })
            Text("Matutino", modifier = Modifier.padding(end = 16.dp))
            RadioButton(selected = turno == "Vespertino", onClick = { turno = "Vespertino" })
            Text("Vespertino")
        }

        // Switch de estatus
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Estatus: ${if (activo) "Activo" else "Inactivo"}")
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        HorizontalDivider()

        Button(
            onClick = {
                errorMatricula = !matriculaRegex.matches(matricula)
                errorNombre = nombre.isBlank()

                if (!errorMatricula && !errorNombre) {
                    val estudiante = Estudiante(matricula, nombre.trim(), carreraSeleccionada, turno, activo)
                    preferencesManager.saveLastRegistro(estudiante)
                    preferencesManager.addEstudiante(estudiante)

                    val ruta = "detalle/${Uri.encode(matricula)}/${Uri.encode(nombre.trim())}/" +
                            "${Uri.encode(carreraSeleccionada)}/${Uri.encode(turno)}/$activo"
                    navController.navigate(ruta)
                } else {
                    Toast.makeText(context, "Revisa los campos marcados", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrar y Ver Detalle") }

        OutlinedButton(
            onClick = { navController.navigate("lista") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ver Lista de Estudiantes") }
    }
}