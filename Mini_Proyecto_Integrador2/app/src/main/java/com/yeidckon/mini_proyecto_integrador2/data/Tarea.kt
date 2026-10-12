package com.yeidckon.mini_proyecto_integrador2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tareas")
data class Tarea(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    // Fecha de vencimiento en milisegundos, tal como la entrega el DatePicker
    val fechaVencimiento: Long,
    val completada: Boolean = false
)