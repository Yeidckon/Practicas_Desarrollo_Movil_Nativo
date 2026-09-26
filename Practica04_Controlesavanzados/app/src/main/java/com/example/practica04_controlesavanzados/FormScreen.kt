package com.example.practica04_controlesavanzados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica04_controlesavanzados.components.CustomCheckbox
import com.example.practica04_controlesavanzados.components.CustomDatePicker
import com.example.practica04_controlesavanzados.components.CustomRadioButton
import com.example.practica04_controlesavanzados.components.CustomSpinner
import com.example.practica04_controlesavanzados.components.CustomSwitch

@Composable
fun FormScreen() {
    var switchState by remember { mutableStateOf(false) }
    var radioOption by remember { mutableStateOf("Opción 1") }
    var checkboxState by remember { mutableStateOf(false) }
    var spinnerText by remember { mutableStateOf("Seleccionar Opción") }
    var dateText by remember { mutableStateOf("Seleccionar Fecha") }
    var showSummary by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Práctica 4: Componentes Avanzados",
            fontSize = 22.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        Text("1. Switch", style = MaterialTheme.typography.titleMedium)
        CustomSwitch(checked = switchState, onCheckedChange = { switchState = it })

        HorizontalDivider()

        Text("2. RadioButton", style = MaterialTheme.typography.titleMedium)
        CustomRadioButton(selectedOption = radioOption, onOptionSelected = { radioOption = it })

        HorizontalDivider()

        Text("3. Checkbox", style = MaterialTheme.typography.titleMedium)
        CustomCheckbox(checked = checkboxState, onCheckedChange = { checkboxState = it })

        HorizontalDivider()

        Text("4. Spinner (DropdownMenu)", style = MaterialTheme.typography.titleMedium)
        CustomSpinner(selectedText = spinnerText, onOptionSelected = { spinnerText = it })

        HorizontalDivider()

        Text("5. DatePicker", style = MaterialTheme.typography.titleMedium)
        CustomDatePicker(selectedDateText = dateText, onDateSelected = { dateText = it })

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showSummary = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Probar Formulario")
        }

        if (showSummary) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen del formulario:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Switch activado: $switchState")
                    Text("Opción seleccionada: $radioOption")
                    Text("Acepta términos: $checkboxState")
                    Text("Spinner: $spinnerText")
                    Text("Fecha: $dateText")
                }
            }
        }
    }
}