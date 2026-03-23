package com.example.calculadoratipoiphone.model

import java.text.DecimalFormat
import kotlin.math.*

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
            .split(Regex("(?<=[0-9.])(?=[+\\-*/^])|(?<=[+\\-*/^])(?=[0-9.])"))
            .filter { it.isNotBlank() }
            .toMutableList()

        // Prioridad muy alta: ^
        var k = 0
        while (k < tokens.size) {
            val op = tokens[k]
            if (op == "^") {
                val base = tokens[k - 1].toDouble()
                val exp = tokens[k + 1].toDouble()
                val res = base.pow(exp)
                tokens[k - 1] = formatoDecimal.format(res)
                tokens.removeAt(k + 1)
                tokens.removeAt(k)
            } else {
                k++
            }
        }

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

    // Funciones matemáticas avanzadas

    fun seno(valor: Double, enGrados: Boolean): Double {
        val radianes = if (enGrados) Math.toRadians(valor) else valor
        return sin(radianes)
    }

    fun coseno(valor: Double, enGrados: Boolean): Double {
        val radianes = if (enGrados) Math.toRadians(valor) else valor
        return cos(radianes)
    }

    fun tangente(valor: Double, enGrados: Boolean): Double {
        val radianes = if (enGrados) Math.toRadians(valor) else valor
        return tan(radianes)
    }

    fun logaritmoBase10(valor: Double): Double = log10(valor)

    fun logaritmoNatural(valor: Double): Double = ln(valor)

    fun cuadrado(valor: Double): Double = valor.pow(2)

    fun raizCuadrada(valor: Double): Double = sqrt(valor)

    fun potencia(base: Double, exponente: Double): Double = base.pow(exponente)
}
