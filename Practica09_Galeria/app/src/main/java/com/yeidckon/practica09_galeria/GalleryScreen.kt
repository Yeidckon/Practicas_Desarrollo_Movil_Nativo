package com.yeidckon.practica09_galeria

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest

// ponytail: URL base fija; mover a BuildConfig si cambia el repo
private const val BASE = "https://raw.githubusercontent.com/Yeidckon/Practicas_Desarrollo_Movil_Nativo/main/Imagenes/"

private val imagenes = (1..7).map { "${BASE}foto$it.jpg" } +
        "https://url-roto.invalid/foto.jpg" // fuerza el estado de error

@Composable
fun GaleriaScreen() {
    // rememberSaveable: el diálogo sobrevive a la rotación
    var seleccionada by rememberSaveable { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().statusBarsPadding().padding(8.dp)) {
        Text("Mi Galería", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 140.dp), // responsiva
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(imagenes) { url -> ImagenItem(url) { seleccionada = url } }
        }
    }

    seleccionada?.let { url ->
        Dialog(
            onDismissRequest = { seleccionada = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Card(Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(12.dp)) {
                AsyncImage(
                    model = url.replace("400/400", "1000/1000"), // versión grande
                    contentDescription = "Imagen ampliada",
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_dialog_alert),
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
            }
        }
    }
}

@Composable
fun ImagenItem(url: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build(),
            contentDescription = "Imagen de la galería",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
            error = painterResource(android.R.drawable.ic_dialog_alert),
            modifier = Modifier.fillMaxSize()
        )
    }
}