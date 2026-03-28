package com.example.calculadoratipoiphone.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun CalcRow(labels: List<Any>, onPress: (Any) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        labels.forEach { label ->
            // Determinar colores según tipo
            val isOperator = label is String && label in listOf("÷", "×", "-", "+", "=")
            val isFunc     = label is String && label in listOf("AC", "⌫", "%", "+/-")
            // Si es icono, asumimos estilo de botón de función (fondo oscuro, icono blanco)
            val isIcon     = label is ImageVector

            val bg = when {
                isOperator -> Naranja
                isFunc || isIcon -> GrisOscuro
                else -> GrisClaro
            }
            val fg = when {
                isOperator || isFunc || isIcon -> Color.White
                else -> Color.Black
            }

            CalcCell(
                label = label, 
                bg = bg, 
                fg = fg, 
                onPress = { onPress(label) },
                onLongPress = {
                    // Si se deja presionado el botón de borrar, forzamos la acción "AC"
                    if (label == "AC" || label == "⌫") {
                        onPress("AC")
                    }
                }
            )
        }
    }
}

/**
 * Botón individual circular de la calculadora con soporte para clic largo.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RowScope.CalcCell(
    label: Any, 
    bg: Color, 
    fg: Color, 
    onPress: () -> Unit,
    onLongPress: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(86.dp)
            .padding(6.dp)
    ) {
        // Se usa Surface en lugar de Button para permitir combinedClickable
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = bg,
            contentColor = fg
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        onClick = onPress,
                        onLongClick = onLongPress
                    ),
                contentAlignment = Alignment.Center
            ) {
                when (label) {
                    is String -> {
                        Text(
                            text = label,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Medium,
                            color = fg
                        )
                    }
                    is ImageVector -> {
                        Icon(
                            imageVector = label,
                            contentDescription = "Calculator Action",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
