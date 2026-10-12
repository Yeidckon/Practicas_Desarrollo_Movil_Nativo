package com.yeidckon.mini_proyecto_integrador2.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeidckon.mini_proyecto_integrador2.data.Tarea
import com.yeidckon.mini_proyecto_integrador2.data.TareaDao

@Database(entities = [Tarea::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tareaDao(): TareaDao

    // Singleton: una sola conexión a la base de datos en toda la app
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tareas_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}