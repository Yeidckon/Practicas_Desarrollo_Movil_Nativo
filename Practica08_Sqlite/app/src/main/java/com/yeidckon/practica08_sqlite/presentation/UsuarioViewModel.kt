package com.yeidckon.practica08_sqlite.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yeidckon.practica08_sqlite.data.Usuario
import com.yeidckon.practica08_sqlite.domain.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).usuarioDao()

    // Convierte el Flow del DAO en un estado que la UI observa
    val usuarios: StateFlow<List<Usuario>> = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertar(nombre: String) {
        viewModelScope.launch { dao.insertar(Usuario(nombre = nombre)) }
    }

    fun actualizar(usuario: Usuario) {
        viewModelScope.launch { dao.actualizar(usuario) }
    }

    fun eliminar(usuario: Usuario) {
        viewModelScope.launch { dao.eliminar(usuario) }
    }
}