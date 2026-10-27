package com.yeidckon.practica12_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yeidckon.practica12_api.presentation.*
import com.yeidckon.practica12_api.ui.theme.Practica12_APITheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel: PokeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica12_APITheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize(), topBar = { TopAppBar(title = { Text("PokeAPI V2") }) }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "tipos",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("tipos") {
                            TiposScreen(viewModel) { id ->
                                navController.navigate("pokemons/$id")
                            }
                        }
                        composable("pokemons/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            PokemonsByTypeScreen(viewModel, id) { pokeId ->
                                navController.navigate("detalle/$pokeId")
                            }
                        }
                        composable("detalle/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id") ?: ""
                            PokemonDetailScreen(viewModel, id)
                        }
                    }
                }
            }
        }
    }
}