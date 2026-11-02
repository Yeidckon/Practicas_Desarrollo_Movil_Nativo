package com.yeidckon.mini_proyecto_integrador3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Publicacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val fotoUri: String,
    val fecha: Long
)