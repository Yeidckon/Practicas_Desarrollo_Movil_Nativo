package com.yeidckon.mini_proyecto_integrador3

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(lista: List<Publicacion>, onNueva: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Red Social Local") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNueva) { Icon(Icons.Default.Add, "Nueva") }
        }
    ) { pad ->
        LazyColumn(
            modifier = Modifier.padding(pad),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lista, key = { it.id }) { p ->
                Card(Modifier.fillMaxWidth()) {
                    Column {
                        AsyncImage(
                            model = p.fotoUri,
                            contentDescription = p.titulo,
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(Modifier.padding(12.dp)) {
                            Text(p.titulo, style = MaterialTheme.typography.titleMedium)
                            Text(formatoFecha(p.fecha), style = MaterialTheme.typography.labelMedium)
                            Text(p.descripcion)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(onGuardar: (Publicacion) -> Unit, onVolver: () -> Unit) {
    val ctx = LocalContext.current
    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var foto by rememberSaveable { mutableStateOf("") }
    var ruta by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableLongStateOf(hoy()) }
    var verFecha by remember { mutableStateOf(false) }

    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        if (ok) foto = Uri.fromFile(File(ruta)).toString()
    }

    fun tomarFoto() {
        val f = nuevaFoto(ctx)
        ruta = f.absolutePath
        camara.launch(FileProvider.getUriForFile(ctx, "${ctx.packageName}.fileprovider", f))
    }

    val permiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
        if (ok) tomarFoto()
        else Toast.makeText(ctx, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
    }

    fun pedirCamara() {
        val concedido = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        if (concedido) tomarFoto() else permiso.launch(Manifest.permission.CAMERA)
    }

    // ponytail: el Photo Picker del sistema no requiere permisos
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) foto = copiarDeGaleria(ctx, uri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva publicación") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            OutlinedButton(onClick = { verFecha = true }) { Text("Fecha: ${formatoFecha(fecha)}") }

            if (foto.isNotEmpty()) {
                AsyncImage(
                    model = foto,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { pedirCamara() }) { Text("Cámara") }
                Button(onClick = {
                    galeria.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) { Text("Galería") }
            }

            Button(
                onClick = { onGuardar(Publicacion(titulo = titulo, descripcion = descripcion, fotoUri = foto, fecha = fecha)) },
                enabled = titulo.isNotBlank() && foto.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar") }
        }
    }

    if (verFecha) {
        val st = rememberDatePickerState(initialSelectedDateMillis = fecha)
        DatePickerDialog(
            onDismissRequest = { verFecha = false },
            confirmButton = {
                TextButton(onClick = { st.selectedDateMillis?.let { fecha = it }; verFecha = false }) { Text("OK") }
            }
        ) { DatePicker(st) }
    }
}