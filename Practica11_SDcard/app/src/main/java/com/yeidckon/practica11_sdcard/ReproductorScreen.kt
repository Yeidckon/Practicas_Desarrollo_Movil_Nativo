package com.yeidckon.practica11_sdcard

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// NUEVO: título del audio. Usa el metadato; si no existe, el nombre del archivo
private fun obtenerTitulo(context: Context, uri: Uri): String {
    val meta = MediaMetadataRetriever()
    val titulo = try {
        meta.setDataSource(context, uri)
        meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
    } catch (e: Exception) {
        null
    } finally {
        meta.release()
    }
    if (!titulo.isNullOrBlank()) return titulo

    return context.contentResolver.query(uri, null, null, null, null)?.use { c ->
        val i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (c.moveToFirst() && i >= 0) c.getString(i) else null
    } ?: "Audio sin título"
}

// NUEVO: milisegundos a mm:ss
private fun Float.mmss() = "%d:%02d".format((this / 60000).toInt(), (this / 1000).toInt() % 60)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReproductorScreen() {
    val context = LocalContext.current

    // Estados para la URI del archivo, el reproductor y si está sonando
    var audioUri by remember { mutableStateOf<Uri?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    // NUEVO: título, posición actual y duración total (en milisegundos)
    var titulo by remember { mutableStateOf("") }
    var posicion by remember { mutableStateOf(0f) }
    var duracion by remember { mutableStateOf(0f) }

    // Lanzador para abrir el explorador de archivos
    val selectorAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            audioUri = uri

            // Liberar el reproductor anterior si existía para evitar fugas de memoria
            mediaPlayer?.release()

            // Crear un nuevo MediaPlayer con el archivo seleccionado
            mediaPlayer = MediaPlayer.create(context, uri)
            mediaPlayer?.start()
            isPlaying = true

            // NUEVO: cargar título y duración, reiniciar la barra
            titulo = obtenerTitulo(context, uri)
            duracion = (mediaPlayer?.duration ?: 0).toFloat()
            posicion = 0f

            // Escuchar cuando la canción termine automáticamente
            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
                posicion = 0f // NUEVO
            }
        }
    }

    // NUEVO: mientras suena, actualiza la barra cada medio segundo
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            posicion = (mediaPlayer?.currentPosition ?: 0).toFloat()
            delay(500)
        }
    }

    // DisposableEffect limpia los recursos del MediaPlayer si el usuario sale de la app
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mini Reproductor MP3") },
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
            verticalArrangement = Arrangement.Center
        ) {
            // NUEVO: muestra el título de la canción en lugar del texto fijo
            Text(
                text = if (audioUri == null) "Ningún archivo seleccionado" else titulo,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para abrir el explorador y filtrado de archivos "solo audios"
            Button(onClick = { selectorAudio.launch("audio/*") }) {
                Text("Buscar MP3 en SD/Memoria")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // NUEVO: barra de progreso; al arrastrarla salta a esa posición
            Slider(
                value = posicion,
                onValueChange = {
                    posicion = it
                    mediaPlayer?.seekTo(it.toInt())
                },
                valueRange = 0f..maxOf(duracion, 1f),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(posicion.mmss(), style = MaterialTheme.typography.bodySmall)
                Text(duracion.mmss(), style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controles de reproducción
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Reproducir
                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && !isPlaying) {
                            mediaPlayer?.start()
                            isPlaying = true
                        }
                    }
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
                }

                // Pausar
                FloatingActionButton(
                    onClick = {
                        if (mediaPlayer != null && isPlaying) {
                            mediaPlayer?.pause()
                            isPlaying = false
                        }
                    }
                ) {
                    Text(" || ", style = MaterialTheme.typography.titleLarge)
                }

                // Detener: pausa y regresa al segundo cero
                FloatingActionButton(
                    onClick = {
                        mediaPlayer?.pause()
                        mediaPlayer?.seekTo(0)
                        isPlaying = false
                        posicion = 0f //La barra vuelve al inicio
                    }
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Detener")
                }
            }
        }
    }
}