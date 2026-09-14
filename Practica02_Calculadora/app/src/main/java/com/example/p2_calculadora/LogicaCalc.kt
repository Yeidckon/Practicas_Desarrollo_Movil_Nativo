package com.example.p2_calculadora

object LogicaCalc {

    fun calcular(num1: String, num2: String, operacion: String): String {
        val n1 = num1.toDoubleOrNull() ?: return ""
        val n2 = num2.toDoubleOrNull() ?: return ""

        val resultado = when (operacion) {
            "+" -> n1 + n2
            "-" -> n1 - n2
            "x" -> n1 * n2
            "÷" -> if (n2 != 0.0) n1 / n2 else Double.NaN
            else -> 0.0
        }

        return if (resultado % 1.0 == 0.0) {
            resultado.toLong().toString()
        } else {
            resultado.toString()
        }
    }
}