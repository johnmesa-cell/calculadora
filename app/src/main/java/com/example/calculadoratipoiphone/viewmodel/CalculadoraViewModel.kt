package com.example.calculadoratipoiphone.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calculadoratipoiphone.model.CalculadoraModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Estado inmutable de la UI.
 */
data class CalculadoraUiState(
    val expresion: String = "0",
    val resultado: String = "",
    val error: Boolean = false,
    val isConvertirEnabled: Boolean = false,
    val isPanelVisible: Boolean = false,
    val historial: List<String> = emptyList(),
    val isDegrees: Boolean = true
) {
    /**
     * true cuando el display está en estado inicial ("0" limpio, sin resultado).
     * La View usa esto para decidir si mostrar "AC" o "⌫" en el botón 1.
     */
    val displayEsInicial: Boolean
        get() = expresion == "0" && resultado.isEmpty() && !error
}

/**
 * VIEWMODEL — reacciona a eventos del usuario y expone estado observable.
 */
class CalculadoraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculadoraUiState())
    val uiState: StateFlow<CalculadoraUiState> = _uiState.asStateFlow()

    private fun ultimoCaracter() = _uiState.value.expresion.lastOrNull()
    private fun esOperador(c: Char?) = c in listOf('+', '-', '×', '÷', '^')

    fun onKey(label: String) {
        when (label) {
            "AC"               -> clearAll()
            "⌫"               -> backspace()
            "+/-"              -> toggleSign()
            "%"                -> percent()
            "."                -> inputDot()
            "="                -> equals()
            "sin"              -> onSin()
            "cos"              -> onCos()
            "tan"              -> onTan()
            "log"              -> onLog()
            "ln"               -> onLn()
            "sqrt"             -> onSqrt()
            "x²"               -> onSquare()
            "x^y"              -> inputOperador("^")
            "π"                -> onPi()
            "deg/rad"          -> toggleDegrees()
            "OPCIONES"         -> { /* próximamente: menú avanzado */ }
            "+", "-", "×", "÷", "^" -> inputOperador(label)
            else               -> if (label.all { it.isDigit() }) inputDigit(label)
        }
    }

    private fun clearAll() {
        _uiState.value = CalculadoraUiState()
    }

    private fun backspace() {
        _uiState.update { s ->
            when {
                s.error                  -> CalculadoraUiState()
                s.resultado.isNotEmpty() -> s.copy(resultado = "")
                else -> s.copy(
                    expresion = if (s.expresion.length <= 1) "0" else s.expresion.dropLast(1)
                )
            }
        }
    }

    private fun inputDigit(d: String) {
        _uiState.update { s ->
            val base = if (s.error) CalculadoraUiState() else s
            if (base.resultado.isNotEmpty()) {
                base.copy(expresion = d, resultado = "")
            } else {
                base.copy(expresion = if (base.expresion == "0") d else base.expresion + d)
            }
        }
    }

    private fun inputDot() {
        _uiState.update { s ->
            if (s.error) return@update CalculadoraUiState()
            val base = if (s.resultado.isNotEmpty()) s.copy(expresion = "0.", resultado = "") else s
            val lastOpIndex = base.expresion.indexOfLast { esOperador(it) }
            val ultimoNumero = if (lastOpIndex == -1) base.expresion
                               else base.expresion.substring(lastOpIndex + 1)
            if (ultimoNumero.contains(".")) base
            else base.copy(
                expresion = if (esOperador(ultimoCaracter())) base.expresion + "0."
                            else base.expresion + "."
            )
        }
    }

    private fun inputOperador(op: String) {
        _uiState.update { s ->
            if (s.error) return@update s
            if (s.resultado.isNotEmpty()) {
                s.copy(expresion = s.resultado + op, resultado = "")
            } else if (esOperador(ultimoCaracter())) {
                s.copy(expresion = s.expresion.dropLast(1) + op)
            } else {
                s.copy(expresion = s.expresion + op)
            }
        }
    }

    private fun toggleSign() {
        _uiState.update { s ->
            if (s.error) return@update s
            val lastOpIndex = s.expresion.indexOfLast { esOperador(it) }
            val nuevaExpr = if (lastOpIndex == -1) {
                if (s.expresion.startsWith("-")) s.expresion.drop(1) else "-${s.expresion}"
            } else {
                val parte = s.expresion.substring(lastOpIndex + 1)
                val nuevaParte = if (parte.startsWith("-")) parte.drop(1) else "-$parte"
                s.expresion.substring(0, lastOpIndex + 1) + nuevaParte
            }
            s.copy(expresion = nuevaExpr)
        }
    }

    private fun percent() {
        _uiState.update { s ->
            if (s.error) return@update s
            val lastOpIndex = s.expresion.indexOfLast { esOperador(it) }
            val ultimoNumero = if (lastOpIndex == -1) s.expresion
                               else s.expresion.substring(lastOpIndex + 1)
            val v = ultimoNumero.toDoubleOrNull() ?: return@update s
            val nuevo = CalculadoraModel.formatear(v / 100.0)
            val nuevaExpr = if (lastOpIndex == -1) nuevo
                            else s.expresion.substring(0, lastOpIndex + 1) + nuevo
            s.copy(expresion = nuevaExpr)
        }
    }

    private fun equals() {
        _uiState.update { s ->
            if (s.error || esOperador(ultimoCaracter())) return@update s
            try {
                val res = CalculadoraModel.evaluar(s.expresion)
                if (res.isNaN()) {
                    s.copy(expresion = "Error", error = true)
                } else {
                    val resStr = CalculadoraModel.formatear(res)
                    val historyEntry = "${s.expresion} = $resStr"
                    s.copy(resultado = resStr, historial = s.historial + historyEntry)
                }
            } catch (_: Exception) {
                s.copy(expresion = "Error", error = true)
            }
        }
    }

    fun toggleConvertir() {
        _uiState.update { it.copy(isConvertirEnabled = !it.isConvertirEnabled) }
    }

    fun togglePanel() {
        _uiState.update { it.copy(isPanelVisible = !it.isPanelVisible) }
    }

    fun toggleDegrees() {
        _uiState.update { it.copy(isDegrees = !it.isDegrees) }
    }

    private fun onPi() {
        _uiState.update { s ->
            if (s.error) return@update s
            val piValue = CalculadoraModel.formatear(kotlin.math.PI)
            val nuevaExpr = if (s.expresion == "0") piValue else s.expresion + piValue
            s.copy(expresion = nuevaExpr)
        }
    }

    private fun onSin() {
        applyUnaryOp("sin") { v -> CalculadoraModel.seno(v, _uiState.value.isDegrees) }
    }
    
    private fun onCos() {
        applyUnaryOp("cos") { v -> CalculadoraModel.coseno(v, _uiState.value.isDegrees) }
    }
    
    private fun onTan() {
        applyUnaryOp("tan") { v -> CalculadoraModel.tangente(v, _uiState.value.isDegrees) }
    }
    
    private fun onLog() {
        applyUnaryOp("log") { v -> CalculadoraModel.logaritmoBase10(v) }
    }
    
    private fun onLn() {
        applyUnaryOp("ln") { v -> CalculadoraModel.logaritmoNatural(v) }
    }
    
    private fun onSqrt() {
        applyUnaryOp("√") { v -> CalculadoraModel.raizCuadrada(v) }
    }
    
    private fun onSquare() {
        applyUnaryOp("sqr") { v -> CalculadoraModel.cuadrado(v) }
    }
    
    private fun applyUnaryOp(opName: String, op: (Double) -> Double) {
        _uiState.update { s ->
            if (s.error) return@update s
            val lastOpIndex = s.expresion.indexOfLast { esOperador(it) }
            val ultimoNumeroStr = if (lastOpIndex == -1) s.expresion
            else s.expresion.substring(lastOpIndex + 1)
            
            val v = ultimoNumeroStr.toDoubleOrNull() ?: return@update s
            
            val res = op(v)
            val resStr = CalculadoraModel.formatear(res)
            
            val historyEntry = "$opName($ultimoNumeroStr) = $resStr"
            val newHistory = s.historial + historyEntry
            
            val nuevaExpr = if (lastOpIndex == -1) resStr
            else s.expresion.substring(0, lastOpIndex + 1) + resStr
            
            s.copy(expresion = nuevaExpr, historial = newHistory)
        }
    }
}
