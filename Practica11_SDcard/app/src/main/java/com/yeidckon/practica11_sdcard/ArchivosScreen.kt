package com.yeidckon.practica11_sdcard

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchivosScreen() {
    val context = LocalContext.current

    // Estados: texto escrito, destino elegido y mensaje de resultado
    var texto by rememberSaveable { mutableStateOf("") }
    var enSd by rememberSaveable { mutableStateOf(false) }
    var resultado by rememberSaveable { mutableStateOf("") }

    // Lanzador para pedir el permiso en tiempo de ejecución
    val pedirPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { ok -> resultado = if (ok) "Permiso concedido" else "Permiso denegado" }

    // Solo en API 28 o menos el permiso de escritura sigue vigente
    LaunchedEffect(Unit) {
        val permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE
        if (Build.VERSION.SDK_INT <= 28 &&
            ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED
        ) pedirPermiso.launch(permiso)
    }

    // ponytail: archivo fijo "nota.txt"; agregar campo de nombre si se pide
    fun archivo(): File {
        val dir = if (!enSd) context.filesDir
        else context.getExternalFilesDirs(null).getOrNull(1) // índice 1 = SD física
            ?: context.getExternalFilesDir(null) ?: context.filesDir
        return File(dir, "nota.txt")
    }

    // Escritura con FileOutputStream (sobrescribe si ya existe)
    fun guardar() {
        resultado = runCatching {
            val f = archivo()
            FileOutputStream(f).use { it.write(texto.toByteArray()) }
            "Guardado en ${f.absolutePath}"
        }.getOrElse { "Error: ${it.message}" }
    }

    // Lectura con FileInputStream
    fun leer() {
        resultado = runCatching {
            val f = archivo()
            "Leído de ${f.absolutePath}:\n" + FileInputStream(f).use { it.readBytes().decodeToString() }
        }.getOrElse { "Error: ${it.message}" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Archivos y SDCard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Texto") }
            )

            // Selector de destino
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = !enSd, onClick = { enSd = false })
                Text("Interna")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = enSd, onClick = { enSd = true })
                Text("SDCard")
            }

            // Acciones
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { guardar() }) { Text("Guardar") }
                Button(onClick = { leer() }) { Text("Leer") }
            }

            Text(text = resultado, style = MaterialTheme.typography.bodyLarge)
        }
    }
}