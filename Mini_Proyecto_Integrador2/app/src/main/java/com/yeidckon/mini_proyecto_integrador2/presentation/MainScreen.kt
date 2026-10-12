package com.yeidckon.mini_proyecto_integrador2.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yeidckon.mini_proyecto_integrador2.data.Tarea
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TareaViewModel = viewModel()) {
    // collectAsState: la lista se redibuja sola al cambiar Room, el filtro o el orden
    val tareas by viewModel.tareas.collectAsState()
    val filtro by viewModel.filtro.collectAsState()
    val orden by viewModel.orden.collectAsState()

    var menuExpandido by remember { mutableStateOf(false) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var tareaEditando by remember { mutableStateOf<Tarea?>(null) } // null = tarea nueva
    var confirmarBorrado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        // --- Filtrar ---
                        Text(
                            "Filtrar",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                        Filtro.entries.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(etiqueta(opcion)) },
                                trailingIcon = {
                                    if (filtro == opcion) Icon(Icons.Default.Check, contentDescription = "Activo")
                                },
                                onClick = {
                                    viewModel.cambiarFiltro(opcion)
                                    menuExpandido = false
                                }
                            )
                        }
                        HorizontalDivider()

                        // --- Ordenar ---
                        Text(
                            "Ordenar",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                        Orden.entries.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(etiqueta(opcion)) },
                                trailingIcon = {
                                    if (orden == opcion) Icon(Icons.Default.Check, contentDescription = "Activo")
                                },
                                onClick = {
                                    viewModel.cambiarOrden(opcion)
                                    menuExpandido = false
                                }
                            )
                        }
                        HorizontalDivider()

                        // --- Acción masiva ---
                        DropdownMenuItem(
                            text = { Text("Borrar completadas") },
                            onClick = {
                                menuExpandido = false
                                confirmarBorrado = true
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                tareaEditando = null
                mostrarDialogo = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { innerPadding ->
        if (tareas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay tareas (${etiqueta(filtro).lowercase()}). Toca + para agregar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                // bottom grande para que el botón flotante no tape la última tarjeta
                contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tareas, key = { it.id }) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onAlternar = { viewModel.alternarCompletada(tarea) },
                        onEditar = {
                            tareaEditando = tarea
                            mostrarDialogo = true
                        },
                        onEliminar = { viewModel.eliminar(tarea) }
                    )
                }
            }
        }
    }

    // Diálogo de crear / editar
    if (mostrarDialogo) {
        TareaDialog(
            tarea = tareaEditando,
            onGuardar = { titulo, descripcion, fecha ->
                val actual = tareaEditando
                if (actual == null) {
                    viewModel.agregar(titulo, descripcion, fecha)
                } else {
                    viewModel.actualizar(
                        actual.copy(titulo = titulo, descripcion = descripcion, fechaVencimiento = fecha)
                    )
                }
                mostrarDialogo = false
            },
            onCancelar = { mostrarDialogo = false }
        )
    }

    // Confirmación antes de borrar completadas
    if (confirmarBorrado) {
        AlertDialog(
            onDismissRequest = { confirmarBorrado = false },
            title = { Text("Borrar completadas") },
            text = { Text("Se eliminarán todas las tareas completadas. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarCompletadas()
                    confirmarBorrado = false
                }) { Text("Borrar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmarBorrado = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun TareaItem(
    tarea: Tarea,
    onAlternar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = tarea.completada, onCheckedChange = { onAlternar() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    // Tachado cuando está completada
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )
                if (tarea.descripcion.isNotBlank()) {
                    Text(text = tarea.descripcion, style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    text = "Vence: ${formatearFecha(tarea.fechaVencimiento)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEditar) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaDialog(
    tarea: Tarea?, // null = nueva tarea
    onGuardar: (String, String, Long) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf(tarea?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tarea?.descripcion ?: "") }
    var fecha by remember { mutableStateOf(tarea?.fechaVencimiento ?: hoyEnUtc()) }
    var mostrarCalendario by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(if (tarea == null) "Nueva tarea" else "Editar tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { mostrarCalendario = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vence: ${formatearFecha(fecha)}")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (titulo.isNotBlank()) onGuardar(titulo, descripcion, fecha)
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )

    if (mostrarCalendario) {
        val estadoFecha = rememberDatePickerState(initialSelectedDateMillis = fecha)
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoFecha.selectedDateMillis?.let { fecha = it }
                    mostrarCalendario = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estadoFecha)
        }
    }
}

private fun etiqueta(filtro: Filtro) = when (filtro) {
    Filtro.TODAS -> "Todas"
    Filtro.PENDIENTES -> "Pendientes"
    Filtro.COMPLETADAS -> "Completadas"
}

private fun etiqueta(orden: Orden) = when (orden) {
    Orden.FECHA -> "Por fecha"
    Orden.ESTADO -> "Por estado"
}

// El DatePicker trabaja en UTC, por eso formateamos en UTC para no perder un día
private fun formatearFecha(millis: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(Date(millis))
}

// Medianoche UTC del día local de hoy (así a las 10 pm en Mazatlán no sale "mañana")
private fun hoyEnUtc(): Long {
    val local = Calendar.getInstance()
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utc.clear()
    utc.set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
    return utc.timeInMillis
}