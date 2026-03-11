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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoratipoiphone.ui.theme.CalculadoraTipoIPhoneTheme
import com.example.calculadoratipoiphone.viewmodel.CalculadoraViewModel

// Paleta de colores (sólo UI)
private val Negro      = Color.Black
private val Blanco     = Color.White
private val Naranja    = Color(0xFFFF9500)
private val GrisOscuro = Color(0xFF333333)
private val GrisClaro  = Color(0xFFBFBFBF)

/**
 * VIEW — punto de entrada de Android. Solo configura el tema y delega al
 * Composable raíz. No contiene ninguna lógica de negocio.
 */
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
                    // El ViewModel se crea/recupera automáticamente
                    val vm: CalculadoraViewModel = viewModel()
                    CalculadoraScreen(
                        modifier = Modifier.padding(padding),
                        viewModel = vm
                    )
                }
            }
        }
    }
}

/**
 * VIEW — Composable raíz. Observa el estado del ViewModel y
 * reenvía los eventos del usuario sin procesarlos.
 */
@Composable
fun CalculadoraScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculadoraViewModel = viewModel()
) {
    // Observa el StateFlow de forma lifecycle-aware
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Negro)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // ── Pantalla ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = state.expresion,
                fontSize = if (state.resultado.isEmpty()) 72.sp else 36.sp,
                color = if (state.resultado.isEmpty()) Blanco else Color(0xFF888888),
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 2,
                softWrap = true
            )
            if (state.resultado.isNotEmpty()) {
                Text(
                    text = state.resultado,
                    fontSize = 72.sp,
                    color = Blanco,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.End,
                    maxLines = 1
                )
            }
        }

        // ── Botones ───────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            CalcRow(listOf("AC", "⌫", "%", "÷")) { viewModel.onKey(it) }
            CalcRow(listOf("7",  "8", "9", "×")) { viewModel.onKey(it) }
            CalcRow(listOf("4",  "5", "6", "-")) { viewModel.onKey(it) }
            CalcRow(listOf("1",  "2", "3", "+")) { viewModel.onKey(it) }
            CalcRow(listOf("+/-","0", ".", "=")) { viewModel.onKey(it) }
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
    CalculadoraTipoIPhoneTheme { CalculadoraScreen() }
}
