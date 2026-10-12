package com.yeidckon.mini_proyecto_integrador2.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yeidckon.mini_proyecto_integrador2.data.Tarea
import com.yeidckon.mini_proyecto_integrador2.domain.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Filtro { TODAS, PENDIENTES, COMPLETADAS }
enum class Orden { FECHA, ESTADO }

class TareaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).tareaDao()

    // Filtro y orden elegidos en el menú. Por defecto: pendientes, por fecha
    private val _filtro = MutableStateFlow(Filtro.PENDIENTES)
    val filtro: StateFlow<Filtro> = _filtro

    private val _orden = MutableStateFlow(Orden.FECHA)
    val orden: StateFlow<Orden> = _orden

    // Combina Room + filtro + orden: si cambia cualquiera, la lista se recalcula
    val tareas: StateFlow<List<Tarea>> =
        combine(dao.obtenerTodas(), _filtro, _orden) { lista, filtro, orden ->
            val filtradas = when (filtro) {
                Filtro.TODAS -> lista
                Filtro.PENDIENTES -> lista.filter { !it.completada }
                Filtro.COMPLETADAS -> lista.filter { it.completada }
            }
            when (orden) {
                Orden.FECHA -> filtradas.sortedBy { it.fechaVencimiento }
                // false va antes que true: pendientes primero, luego por fecha
                Orden.ESTADO -> filtradas.sortedWith(
                    compareBy<Tarea> { it.completada }.thenBy { it.fechaVencimiento }
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarFiltro(nuevo: Filtro) { _filtro.value = nuevo }
    fun cambiarOrden(nuevo: Orden) { _orden.value = nuevo }

    fun agregar(titulo: String, descripcion: String, fecha: Long) {
        viewModelScope.launch {
            dao.insertar(Tarea(titulo = titulo, descripcion = descripcion, fechaVencimiento = fecha))
        }
    }

    fun actualizar(tarea: Tarea) {
        viewModelScope.launch { dao.actualizar(tarea) }
    }

    fun alternarCompletada(tarea: Tarea) {
        viewModelScope.launch { dao.actualizar(tarea.copy(completada = !tarea.completada)) }
    }

    fun eliminar(tarea: Tarea) {
        viewModelScope.launch { dao.eliminar(tarea) }
    }

    fun eliminarCompletadas() {
        viewModelScope.launch { dao.eliminarCompletadas() }
    }
}