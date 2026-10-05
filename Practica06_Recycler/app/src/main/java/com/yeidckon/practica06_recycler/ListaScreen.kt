package com.yeidckon.practica06_recycler

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListaScreen() {
    val contactos = obtenerContactosDummy()

    // Guarda el contacto seleccionado; null = ninguna ventana abierta
    var contactoSeleccionado by remember { mutableStateOf<Contacto?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(contactos) { contacto ->
            ContactoItem(
                contacto = contacto,
                onClick = { contactoSeleccionado = contacto }
            )
        }
    }

    // Si hay un contacto seleccionado, mostramos su ventana de información
    contactoSeleccionado?.let { contacto ->
        Mostrar_info(contacto = contacto, onDismiss = { contactoSeleccionado = null })
    }
}

@Composable
fun ContactoItem(contacto: Contacto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE3F2FD))
                .padding(16.dp)
        ) {
            Text(
                text = contacto.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tel: ${contacto.telefono}",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

// Ventana emergente con la información completa del contacto
@Composable
fun Mostrar_info(contacto: Contacto, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(contacto.nombre) },
        text = {
            Column {
                Text("ID: ${contacto.id}")
                Spacer(modifier = Modifier.height(4.dp))
                Text("Teléfono: ${contacto.telefono}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}