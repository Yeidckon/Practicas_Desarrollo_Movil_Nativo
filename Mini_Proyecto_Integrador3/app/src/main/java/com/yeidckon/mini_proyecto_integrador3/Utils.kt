package com.yeidckon.mini_proyecto_integrador3

import android.content.Context
import android.net.Uri
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun nuevaFoto(c: Context): File {
    val dir = File(c.filesDir, "fotos").apply { mkdirs() }
    return File(dir, "foto_${System.currentTimeMillis()}.jpg")
}

// ponytail: copia la imagen de galería a almacenamiento propio para que no caduque el permiso
fun copiarDeGaleria(c: Context, uri: Uri): String {
    val f = nuevaFoto(c)
    c.contentResolver.openInputStream(uri)!!.use { i -> f.outputStream().use { o -> i.copyTo(o) } }
    return Uri.fromFile(f).toString()
}

// ponytail: el DatePicker trabaja en UTC; todo se guarda y muestra en UTC para evitar el día de menos
fun hoy(): Long {
    val l = Calendar.getInstance()
    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(l.get(Calendar.YEAR), l.get(Calendar.MONTH), l.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}

fun formatoFecha(ms: Long): String =
    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("UTC") }
        .format(ms)