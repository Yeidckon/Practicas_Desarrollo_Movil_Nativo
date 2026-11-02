package com.yeidckon.mini_proyecto_integrador3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yeidckon.mini_proyecto_integrador3.ui.theme.Mini_Proyecto_Integrador3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Mini_Proyecto_Integrador3Theme { App() } }
    }
}

@Composable
fun App(vm: PublicacionViewModel = viewModel()) {
    var enForm by rememberSaveable { mutableStateOf(false) }
    val lista by vm.publicaciones.collectAsStateWithLifecycle()

    BackHandler(enForm) { enForm = false }

    // ponytail: dos pantallas, un booleano; Navigation sobra aquí
    if (enForm) FormularioScreen(onGuardar = { vm.guardar(it); enForm = false }, onVolver = { enForm = false })
    else ListaScreen(lista) { enForm = true }
}