package com.example.calculadoratipoiphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoratipoiphone.ui.theme.CalculadoraTipoIPhoneTheme
import java.text.DecimalFormat

// Colores
private val Negro = Color.Black
private val Blanco = Color.White
private val Naranja = Color(0xFFFF9500)
private val GrisOscuro = Color(0xFF333333)
private val GrisClaro = Color(0xFFBFBFBF)

// ✅ DecimalFormat global (no dentro del composable)
private val formatoDecimal = DecimalFormat("#.##########")

// ✅ PARSER DE JERARQUÍAS (función global, fuera del composable)
fun evaluarCadenaCompleta(expresion: String): Double {
    // 1. Limpieza de símbolos
    val limpia = expresion
        .replace("×", "*")
        .replace("÷", "/")
        .replace(" ", "")

    // 2. Tokenizar: separa números de operadores
    // Regex que corta antes/después de + - * / pero NO separa el - inicial de un número negativo
    val tokens = limpia.split(Regex("(?<=[0-9.])(?=[+\\-*/])|(?<=[+\\-*/])(?=[0-9.])"))
        .filter { it.isNotBlank() }
        .toMutableList()

    // 3. PRIMERO: × ÷ (alta prioridad)
    var i = 0
    while (i < tokens.size) {
        val oper = tokens[i]
        if (oper == "*" || oper == "/") {
            val num1 = tokens[i - 1].toDouble()
            val num2 = tokens[i + 1].toDouble()
            val res = when (oper) {
                "*"  -> num1 * num2
                "/"  -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }
            tokens[i - 1] = formatoDecimal.format(res)
            tokens.removeAt(i + 1)
            tokens.removeAt(i)
            // NO incrementar i porque el array se achicó
        } else {
            i++
        }
    }

    // 4. DESPUÉS: + - (baja prioridad)
    var resultado = tokens[0].toDouble()
    for (j in 1 until tokens.size step 2) {
        val op  = tokens[j]
        val valor = tokens[j + 1].toDouble()
        when (op) {
            "+" -> resultado += valor
            "-" -> resultado -= valor
        }
    }
    return resultado
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTipoIPhoneTheme {
                Scaffold(
                    containerColor = Negro,
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
                    CalculadoraApple(modifier = Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
fun CalculadoraApple(modifier: Modifier = Modifier) {

    // ✅ UNA SOLA variable: toda la expresión como cadena
    var expr by remember { mutableStateOf("0") }
    var resultado by remember { mutableStateOf("") } // muestra el resultado tras "="
    var error by remember { mutableStateOf(false) }

    fun ultimoCaracter() = expr.lastOrNull()
    fun esOperador(c: Char?) = c == '+' || c == '-' || c == '×' || c == '÷'

    fun clearAll() {
        expr = "0"
        resultado = ""
        error = false
    }

    fun backspace() {
        if (error) { clearAll(); return }
        if (resultado.isNotEmpty()) {
            // Si ya calculó, borra el resultado y vuelve a la expresión
            resultado = ""
            return
        }
        expr = if (expr.length <= 1) "0" else expr.dropLast(1)
    }

    fun inputDigit(d: String) {
        if (error) clearAll()
        if (resultado.isNotEmpty()) {
            // Después de "=" empieza nueva expresión
            expr = d
            resultado = ""
            return
        }
        expr = if (expr == "0") d else expr + d
    }

    fun inputDot() {
        if (error) clearAll()
        if (resultado.isNotEmpty()) { expr = "0."; resultado = ""; return }

        // Busca el último número (después del último operador)
        val lastOpIndex = expr.indexOfLast { esOperador(it) }
        val ultimoNumero = if (lastOpIndex == -1) expr else expr.substring(lastOpIndex + 1)

        // Solo agrega "." si el número actual no tiene uno ya
        if (!ultimoNumero.contains(".")) {
            expr = if (esOperador(ultimoCaracter())) expr + "0." else expr + "."
        }
    }

    fun inputOperador(op: String) {
        if (error) return

        if (resultado.isNotEmpty()) {
            // Encadena desde el resultado
            expr = resultado + op
            resultado = ""
            return
        }

        // Si el último carácter ya es operador, lo reemplaza
        if (esOperador(ultimoCaracter())) {
            expr = expr.dropLast(1) + op
        } else {
            expr += op
        }
    }

    fun toggleSign() {
        if (error) return
        // Busca el último número y le cambia el signo
        val lastOpIndex = expr.indexOfLast { esOperador(it) }
        if (lastOpIndex == -1) {
            // Solo hay un número
            expr = if (expr.startsWith("-")) expr.drop(1) else "-$expr"
        } else {
            val parte = expr.substring(lastOpIndex + 1)
            val nuevaParte = if (parte.startsWith("-")) parte.drop(1) else "-$parte"
            expr = expr.substring(0, lastOpIndex + 1) + nuevaParte
        }
    }

    fun percent() {
        if (error) return
        val lastOpIndex = expr.indexOfLast { esOperador(it) }
        val ultimoNumero = if (lastOpIndex == -1) expr else expr.substring(lastOpIndex + 1)
        val v = ultimoNumero.toDoubleOrNull() ?: return
        val nuevo = formatoDecimal.format(v / 100.0)
        expr = if (lastOpIndex == -1) nuevo else expr.substring(0, lastOpIndex + 1) + nuevo
    }

    fun equals() {
        if (error) return
        if (esOperador(ultimoCaracter())) return // expresión incompleta

        try {
            val res = evaluarCadenaCompleta(expr)
            if (res.isNaN()) {
                error = true
                expr = "Error"
            } else {
                resultado = formatoDecimal.format(res)
            }
        } catch (_: Exception) {
            error = true
            expr = "Error"
        }
    }

    fun onKey(label: String) {
        when (label) {
            "AC"              -> clearAll()
            "⌫"              -> backspace()
            "+/-"             -> toggleSign()
            "%"               -> percent()
            "."               -> inputDot()
            "="               -> equals()
            "+", "-", "×", "÷" -> inputOperador(label)
            else              -> if (label.all { it.isDigit() }) inputDigit(label)
        }
    }

    // ---- UI ----
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Negro)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Pantalla: muestra expresión y resultado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Expresión completa (ej: 2+6-4/9×4)
            Text(
                text = expr,
                fontSize = if (resultado.isEmpty()) 72.sp else 36.sp,
                color = if (resultado.isEmpty()) Blanco else Color(0xFF888888),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 2,
                softWrap = true
            )

            // Resultado (aparece al presionar =)
            if (resultado.isNotEmpty()) {
                Text(
                    text = resultado,
                    fontSize = 72.sp,
                    color = Blanco,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }
        }

        // Grilla de botones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            CalcRow(listOf("AC", "⌫", "%", "÷")) { onKey(it) }
            CalcRow(listOf("7",  "8", "9", "×")) { onKey(it) }
            CalcRow(listOf("4",  "5", "6", "-")) { onKey(it) }
            CalcRow(listOf("1",  "2", "3", "+")) { onKey(it) }
            CalcRow(listOf("+/-","0", ".", "=")) { onKey(it) }
        }
    }
}

@Composable
fun CalcRow(labels: List<String>, onPress: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        labels.forEach { label ->
            val isOperator = label in listOf("÷", "×", "-", "+", "=")
            val isFunc     = label in listOf("AC", "⌫", "%", "+/-")
            val bg = when { isOperator -> Naranja; isFunc -> GrisOscuro; else -> GrisClaro }
            val fg = when { isOperator -> Blanco;  isFunc -> Blanco;     else -> Color.Black }
            CalcCell(label = label, bg = bg, fg = fg) { onPress(label) }
        }
    }
}

@Composable
fun RowScope.CalcCell(label: String, bg: Color, fg: Color, onPress: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(86.dp)
            .padding(6.dp)
    ) {
        Button(
            onClick = onPress,
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = bg),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = label, fontSize = 26.sp, fontWeight = FontWeight.Medium, color = fg)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalc() {
    CalculadoraTipoIPhoneTheme { CalculadoraApple() }
}