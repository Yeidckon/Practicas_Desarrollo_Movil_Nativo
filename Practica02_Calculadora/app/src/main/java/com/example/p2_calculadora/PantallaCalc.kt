package com.example.p2_calculadora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
val CalcBG = Color(0xFF094933)
val DarkSC = Color(0xFF282834)
val BotonClr = Color(0xFF087049)
val AppBG = Color(0xFF1B2E4B)
@Composable
fun CalcBtn(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BotonClr)
            .clickable { onClick() }
    ) {
        Text(text = text, fontSize = 22.sp, color = Color.White)
    }
}

@Composable
fun CalculadoraPantalla(){
    var n1 by remember { mutableStateOf("") }
    var n2 by remember { mutableStateOf("") }
    var opc by remember { mutableStateOf("") }

    fun onButtonClick(simbolo: String){
        when (simbolo) {
            "C" -> {
                n1 = ""
                n2 = ""
                opc = ""
            }

            "←" -> {
                if (n2.isNotEmpty()) n2 = n2.dropLast(1)
                else if (opc.isNotEmpty()) opc = ""
                else if (n1.isNotEmpty()) n1 = n1.dropLast(1)
            }

            "+", "-", "x", "÷" -> {
                if (n1.isNotEmpty()) opc = simbolo
            }

            "=" -> {
                if (n1.isNotEmpty() && n2.isNotEmpty() && opc.isNotEmpty()) {
                    n1 = LogicaCalc.calcular(n1, n2, opc)
                    n2 = ""
                    opc = ""
                }
            }

            else -> {
                if (opc.isEmpty()) {
                    if (n1.length < 8) n1 += simbolo
                } else {
                    if (n2.length < 8) n2 += simbolo
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBG)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(320.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(CalcBG)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkSC)
                    .padding(20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Text(
                    text = "$n1 $opc $n2",
                    color = Color.White,
                    fontSize = 32.sp
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CalcBtn("C", Modifier.weight(2f)) { onButtonClick("C") }
                    CalcBtn("←", Modifier.weight(1f)) { onButtonClick("←") }
                    CalcBtn("%", Modifier.weight(1f)) { onButtonClick("%") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CalcBtn("7", Modifier.weight(1f)) { onButtonClick("7") }
                    CalcBtn("8", Modifier.weight(1f)) { onButtonClick("8") }
                    CalcBtn("9", Modifier.weight(1f)) { onButtonClick("9") }
                    CalcBtn("x", Modifier.weight(1f)) { onButtonClick("x") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CalcBtn("4", Modifier.weight(1f)) { onButtonClick("4") }
                    CalcBtn("5", Modifier.weight(1f)) { onButtonClick("5") }
                    CalcBtn("6", Modifier.weight(1f)) { onButtonClick("6") }
                    CalcBtn("-", Modifier.weight(1f)) { onButtonClick("-") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CalcBtn("1", Modifier.weight(1f)) { onButtonClick("1") }
                    CalcBtn("2", Modifier.weight(1f)) { onButtonClick("2") }
                    CalcBtn("3", Modifier.weight(1f)) { onButtonClick("3") }
                    CalcBtn("+", Modifier.weight(1f)) { onButtonClick("+") }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CalcBtn("0", Modifier.weight(2f)) { onButtonClick("0") }
                    CalcBtn(".", Modifier.weight(1f)) { onButtonClick(".") }
                    CalcBtn("÷", Modifier.weight(1f)) { onButtonClick("÷") }
                    CalcBtn("=", Modifier.weight(1f)) { onButtonClick("=") }
                }
            }
        }
    }
}

