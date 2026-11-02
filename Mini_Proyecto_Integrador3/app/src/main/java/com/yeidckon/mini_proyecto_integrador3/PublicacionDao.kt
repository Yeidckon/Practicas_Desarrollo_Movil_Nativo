package com.yeidckon.mini_proyecto_integrador3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PublicacionDao {
    @Query("SELECT * FROM Publicacion ORDER BY fecha DESC, id DESC")
    fun todas(): Flow<List<Publicacion>>

    @Insert
    suspend fun insertar(p: Publicacion)
}