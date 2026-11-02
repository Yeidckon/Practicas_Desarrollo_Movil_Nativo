package com.yeidckon.mini_proyecto_integrador3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Publicacion::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): PublicacionDao

    companion object {
        @Volatile private var inst: AppDatabase? = null

        // ponytail: singleton simple, sin inyección de dependencias
        fun get(c: Context): AppDatabase = inst ?: synchronized(this) {
            Room.databaseBuilder(c.applicationContext, AppDatabase::class.java, "red_social.db")
                .build().also { inst = it }
        }
    }
}