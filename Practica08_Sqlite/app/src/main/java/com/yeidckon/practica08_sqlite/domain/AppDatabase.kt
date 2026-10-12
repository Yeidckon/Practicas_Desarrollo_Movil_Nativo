package com.yeidckon.practica08_sqlite.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeidckon.practica08_sqlite.data.Usuario
import com.yeidckon.practica08_sqlite.data.UsuarioDao

@Database(entities = [Usuario::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    // Singleton: una sola conexión a la base de datos en toda la app
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mi_base_datos"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}