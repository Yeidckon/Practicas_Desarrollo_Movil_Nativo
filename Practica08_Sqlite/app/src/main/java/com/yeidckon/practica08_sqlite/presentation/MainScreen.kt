package com.yeidckon.practica08_sqlite.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yeidckon.practica08_sqlite.data.Usuario

@Composable
fun MainScreen(viewModel: UsuarioViewModel = viewModel()) {
    // collectAsState() convierte el Flow en estado: la UI se redibuja sola
    val usuarios by viewModel.usuarios.collectAsState()

    var nuevoNombre by remember { mutableStateOf("") }
    // Usuario que se está editando; null = diálogo cerrado
    var usuarioEditando by remember { mutableStateOf<Usuario?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        OutlinedTextField(
            value = nuevoNombre,
            onValueChange = { nuevoNombre = it },
            label = { Text("Nombre del usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nuevoNombre.isNotBlank()) {
                    viewModel.insertar(nuevoNombre)
                    nuevoNombre = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Lista de Usuarios Registrados:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(usuarios, key = { it.id }) { usuario ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ID: ${usuario.id} | Nombre: ${usuario.nombre}",
                            modifier = Modifier.weight(1f)
                        )
                        // Editar: abre el diálogo con este usuario
                        IconButton(onClick = { usuarioEditando = usuario }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        // Eliminar: el ViewModel lanza la corrutina
                        IconButton(onClick = { viewModel.eliminar(usuario) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }
    }

    usuarioEditando?.let { usuario ->
        EditarUsuarioDialog(
            usuario = usuario,
            onGuardar = {
                viewModel.actualizar(it)
                usuarioEditando = null
            },
            onCancelar = { usuarioEditando = null }
        )
    }
}

@Composable
fun EditarUsuarioDialog(
    usuario: Usuario,
    onGuardar: (Usuario) -> Unit,
    onCancelar: () -> Unit
) {
    var nombre by remember { mutableStateOf(usuario.nombre) }

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("Editar usuario") },
        text = {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del usuario") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = {
                if (nombre.isNotBlank()) onGuardar(usuario.copy(nombre = nombre))
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) { Text("Cancelar") }
        }
    )
}