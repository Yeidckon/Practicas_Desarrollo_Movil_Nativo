package com.yeidckon.mini_proyecto_integrador.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import com.yeidckon.mini_proyecto_integrador.model.Estudiante
class PreferencesManager(context: Context) {

    private val prefs = context.getSharedPreferences("RegistroPrefs", Context.MODE_PRIVATE)

    companion object {
        const val KEY_MATRICULA = "last_matricula"
        const val KEY_NOMBRE = "last_nombre"
        const val KEY_CARRERA = "last_carrera"
        const val KEY_TURNO = "last_turno"
        const val KEY_ACTIVO = "last_activo"
        const val KEY_LISTA = "lista_estudiantes"
    }

    // --- Último registro (para precargar el formulario) ---
    fun saveLastRegistro(e: Estudiante) {
        prefs.edit()
            .putString(KEY_MATRICULA, e.matricula)
            .putString(KEY_NOMBRE, e.nombre)
            .putString(KEY_CARRERA, e.carrera)
            .putString(KEY_TURNO, e.turno)
            .putBoolean(KEY_ACTIVO, e.activo)
            .commit()
    }

    fun getLastRegistro(): Estudiante {
        return Estudiante(
            matricula = prefs.getString(KEY_MATRICULA, "") ?: "",
            nombre = prefs.getString(KEY_NOMBRE, "") ?: "",
            carrera = prefs.getString(KEY_CARRERA, "") ?: "",
            turno = prefs.getString(KEY_TURNO, "Matutino") ?: "Matutino",
            activo = prefs.getBoolean(KEY_ACTIVO, true)
        )
    }

    // --- Lista completa de estudiantes (persistida como JSON) ---
    fun addEstudiante(e: Estudiante) {
        val lista = getEstudiantes().toMutableList()
        lista.add(e)
        saveEstudiantes(lista)
    }

    private fun saveEstudiantes(lista: List<Estudiante>) {
        val array = JSONArray()
        lista.forEach {
            val obj = JSONObject()
            obj.put("matricula", it.matricula)
            obj.put("nombre", it.nombre)
            obj.put("carrera", it.carrera)
            obj.put("turno", it.turno)
            obj.put("activo", it.activo)
            array.put(obj)
        }
        prefs.edit().putString(KEY_LISTA, array.toString()).commit()
    }

    fun getEstudiantes(): List<Estudiante> {
        val json = prefs.getString(KEY_LISTA, null) ?: return emptyList()
        val array = JSONArray(json)
        return List(array.length()) { i ->
            val obj = array.getJSONObject(i)
            Estudiante(
                matricula = obj.getString("matricula"),
                nombre = obj.getString("nombre"),
                carrera = obj.getString("carrera"),
                turno = obj.getString("turno"),
                activo = obj.getBoolean("activo")
            )
        }
    }

    fun clearAll() {
        prefs.edit().clear().commit()
    }
}