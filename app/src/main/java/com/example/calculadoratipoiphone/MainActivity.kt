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
    var left by remember { mutableStateOf("0") }
    var right by remember { mutableStateOf("") }
    var op by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf(false) }

    fun editingRight() = op != null
    fun parseNumber(s: String) = s.toDoubleOrNull()

    fun formatNumber(d: Double): String {
        val df = DecimalFormat("#.###############")
        return df.format(d)
    }

    fun currentEditable() = if (editingRight()) right else left
    fun setCurrentEditable(v: String) {
        if (editingRight()) right = v else left = v
    }

    fun clearAll() {
        left = "0"
        right = ""
        op = null
        error = false
    }

    fun backspace() {
        if (error) { clearAll(); return }

        if (editingRight() && right.isEmpty()) {
            op = null
            return
        }

        val cur = currentEditable()
        val newValue = if (cur.length <= 1) "0" else cur.dropLast(1)
        setCurrentEditable(if (newValue == "-") "0" else newValue)

        if (editingRight() && right == "0") right = ""
    }

    fun inputDigit(d: String) {
        if (error) clearAll()
        val cur = currentEditable()
        val next = if (cur == "0") d else cur + d
        setCurrentEditable(next)
    }

    fun inputDot() {
        if (error) clearAll()
        val cur = currentEditable()
        if (cur.contains(".")) return
        val next = if (cur.isEmpty() || cur == "0") "0." else "$cur."
        setCurrentEditable(next)
    }

    fun toggleSign() {
        if (error) return
        val cur = currentEditable().ifEmpty { "0" }
        if (cur == "0") return
        val next = if (cur.startsWith("-")) cur.drop(1) else "-$cur"
        setCurrentEditable(next)
    }

    fun percent() {
        if (error) return
        val cur = currentEditable().ifEmpty { "0" }
        val v = parseNumber(cur) ?: return
        val next = formatNumber(v / 100.0)
        setCurrentEditable(next)
        if (editingRight() && right.isEmpty()) right = "0"
    }

    fun compute(a: Double, b: Double, oper: String): Double? =
        when (oper) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> if (b == 0.0) null else a / b
            else -> b
        }

    fun applyOperator(newOp: String) {
        if (error) return

        if (op == null) {
            op = newOp
            right = ""
            return
        }

        if (right.isEmpty() || right == "-") {
            op = newOp
            return
        }

        val a = parseNumber(left) ?: return
        val b = parseNumber(right) ?: return
        val res = compute(a, b, op!!) ?: run {
            error = true
            left = "Error"
            right = ""
            op = null
            return
        }

        left = formatNumber(res)
        right = ""
        op = newOp
    }

    fun equals() {
        if (error) return
        val oper = op ?: return
        if (right.isEmpty() || right == "-") return

        val a = parseNumber(left) ?: return
        val b = parseNumber(right) ?: return
        val res = compute(a, b, oper) ?: run {
            error = true
            left = "Error"
            right = ""
            op = null
            return
        }

        left = formatNumber(res)
        right = ""
        op = null
    }

    fun displayExpr(): String {
        val safeLeft = left.ifEmpty { "0" }
        val safeOp = op ?: ""
        val safeRight = right
        return (safeLeft + safeOp + safeRight).ifBlank { "0" }
    }

    fun onKey(label: String) {
        when (label) {
            "AC" -> clearAll()
            "⌫" -> backspace()
            "+/-" -> toggleSign()
            "%" -> percent()
            "." -> inputDot()
            "=" -> equals()
            "+", "-", "×", "÷" -> applyOperator(label)
            else -> if (label.all { it.isDigit() }) inputDigit(label)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Negro)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = displayExpr(),
                fontSize = 78.sp,
                color = Blanco,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            CalcRow(listOf("AC", "⌫", "%", "÷"), ::onKey)
            CalcRow(listOf("7", "8", "9", "×"), ::onKey)
            CalcRow(listOf("4", "5", "6", "-"), ::onKey)
            CalcRow(listOf("1", "2", "3", "+"), ::onKey)
            CalcRow(listOf("+/-", "0", ".", "="), ::onKey)
        }
    }
}

@Composable
fun CalcRow(labels: List<String>, onPress: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        labels.forEach { label ->
            val isOperator = label in listOf("÷", "×", "-", "+", "=")
            val isFunc = label in listOf("AC", "⌫", "%", "+/-")

            val bg = when {
                isOperator -> Naranja
                isFunc -> GrisOscuro
                else -> GrisClaro
            }

            val fg = when {
                isOperator -> Blanco
                isFunc -> Blanco
                else -> Color.Black
            }

            CalcCell(label = label, bg = bg, fg = fg) { onPress(label) }
        }
    }
}

@Composable
fun RowScope.CalcCell(
    label: String,
    bg: Color,
    fg: Color,
    onPress: () -> Unit
) {
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
            Text(
                text = label,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                color = fg
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalc() {
    CalculadoraTipoIPhoneTheme { CalculadoraApple() }
}
