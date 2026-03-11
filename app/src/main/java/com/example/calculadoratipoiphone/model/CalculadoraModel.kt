package com.example.calculadoratipoiphone.model

import java.text.DecimalFormat

/**
 * MODEL — lógica de negocio pura, sin dependencias de Android/Compose.
 */
object CalculadoraModel {

    private val formatoDecimal = DecimalFormat("#.##########")

    fun formatear(valor: Double): String = formatoDecimal.format(valor)

    /**
     * Evalúa una expresión matemática (ej: "2+6×4÷2-1") respetando
     * la jerarquía de operaciones: primero × ÷, luego + -.
     */
    fun evaluar(expresion: String): Double {
        val limpia = expresion
            .replace("×", "*")
            .replace("÷", "/")
            .replace(" ", "")

        val tokens = limpia
            .split(Regex("(?<=[0-9.])(?=[+\\-*/])|(?<=[+\\-*/])(?=[0-9.])"))
            .filter { it.isNotBlank() }
            .toMutableList()

        // Prioridad alta: * /
        var i = 0
        while (i < tokens.size) {
            val op = tokens[i]
            if (op == "*" || op == "/") {
                val a = tokens[i - 1].toDouble()
                val b = tokens[i + 1].toDouble()
                val res = if (op == "*") a * b else if (b != 0.0) a / b else Double.NaN
                tokens[i - 1] = formatoDecimal.format(res)
                tokens.removeAt(i + 1)
                tokens.removeAt(i)
            } else {
                i++
            }
        }

        // Prioridad baja: + -
        var resultado = tokens[0].toDouble()
        var j = 1
        while (j < tokens.size) {
            val op = tokens[j]
            val valor = tokens[j + 1].toDouble()
            when (op) {
                "+" -> resultado += valor
                "-" -> resultado -= valor
            }
            j += 2
        }
        return resultado
    }
}
