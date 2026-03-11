package com.example.calculadoratipoiphone.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Paleta interna de este componente
private val Naranja    = Color(0xFFFF9500)
private val GrisOscuro = Color(0xFF333333)
private val GrisClaro  = Color(0xFFBFBFBF)

/**
 * Fila de botones de la calculadora.
 * Recibe la lista de etiquetas y el callback de pulsación.
 */
@Composable
fun CalcRow(labels: List<String>, onPress: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        labels.forEach { label ->
            val isOperator = label in listOf("÷", "×", "-", "+", "=")
            val isFunc     = label in listOf("AC", "⌫", "%", "+/-")
            val bg = when { isOperator -> Naranja; isFunc -> GrisOscuro; else -> GrisClaro }
            val fg = when { isOperator -> Color.White; isFunc -> Color.White; else -> Color.Black }
            CalcCell(label = label, bg = bg, fg = fg) { onPress(label) }
        }
    }
}

/**
 * Botón individual circular de la calculadora.
 */
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
            Text(
                text = label,
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                color = fg
            )
        }
    }
}
