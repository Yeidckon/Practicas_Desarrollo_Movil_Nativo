package com.yeidckon.mini_proyecto_integrador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.yeidckon.mini_proyecto_integrador.ui.theme.MiniProyectoIntegradorTheme
import com.yeidckon.mini_proyecto_integrador.ui.RegistroScreen
import com.yeidckon.mini_proyecto_integrador.ui.DetalleScreen
import com.yeidckon.mini_proyecto_integrador.ui.ListaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniProyectoIntegradorTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "registro") {

                    composable("registro") {
                        RegistroScreen(navController)
                    }

                    composable("lista") {
                        ListaScreen(navController)
                    }

                    composable(
                        route = "detalle/{matricula}/{nombre}/{carrera}/{turno}/{activo}",
                        arguments = listOf(
                            navArgument("matricula") { type = NavType.StringType },
                            navArgument("nombre") { type = NavType.StringType },
                            navArgument("carrera") { type = NavType.StringType },
                            navArgument("turno") { type = NavType.StringType },
                            navArgument("activo") { type = NavType.BoolType }
                        )
                    ) { backStackEntry ->
                        DetalleScreen(
                            matricula = backStackEntry.arguments?.getString("matricula") ?: "",
                            nombre = backStackEntry.arguments?.getString("nombre") ?: "",
                            carrera = backStackEntry.arguments?.getString("carrera") ?: "",
                            turno = backStackEntry.arguments?.getString("turno") ?: "",
                            activo = backStackEntry.arguments?.getBoolean("activo") ?: false,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}