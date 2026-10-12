package com.yeidckon.mini_proyecto_integrador2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    // El filtro y el orden los aplica el ViewModel sobre esta lista
    @Query("SELECT * FROM tareas")
    fun obtenerTodas(): Flow<List<Tarea>>

    @Insert
    suspend fun insertar(tarea: Tarea)

    @Update
    suspend fun actualizar(tarea: Tarea)

    @Delete
    suspend fun eliminar(tarea: Tarea)

    // Room guarda el Boolean como 0 o 1
    @Query("DELETE FROM tareas WHERE completada = 1")
    suspend fun eliminarCompletadas()
}