package com.example.practica04_controlesavanzados.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(selectedOption: String, onOptionSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = (selectedOption == "Opción 1"), onClick = { onOptionSelected("Opción 1") })
        Text("Opción 1", modifier = Modifier.clickable { onOptionSelected("Opción 1") })

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(selected = (selectedOption == "Opción 2"), onClick = { onOptionSelected("Opción 2") })
        Text("Opción 2", modifier = Modifier.clickable { onOptionSelected("Opción 2") })
    }
}