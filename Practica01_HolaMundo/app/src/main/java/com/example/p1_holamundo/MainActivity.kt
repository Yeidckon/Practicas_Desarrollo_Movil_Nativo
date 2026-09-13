 package com.example.p1_holamundo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.p1_holamundo.ui.theme.P1_HolaMundoTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            P1_HolaMundoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var textoIn by remember {mutableStateOf("")}
    var saludo by remember {mutableStateOf("")}
    Column(modifier = modifier) {
        TextField(
            value = textoIn,
            onValueChange =  {textoIn = it},
            label = {Text("Ingresa tu nombre:")}
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {saludo = "¡Hola, $textoIn!"}){
            Text("Saludar")
        }
        Text(text = saludo)
    }
 }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    P1_HolaMundoTheme {
        Greeting()
    }
}