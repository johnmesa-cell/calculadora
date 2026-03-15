package com.example.calculadoratipoiphone.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Calculate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.material3.Text
import com.example.calculadoratipoiphone.ui.theme.CalculadoraTipoIPhoneTheme
import com.example.calculadoratipoiphone.view.components.CalcRow
import com.example.calculadoratipoiphone.view.components.ConvertirPanel
import com.example.calculadoratipoiphone.viewmodel.CalculadoraViewModel

internal val Negro  = Color.Black
internal val Blanco = Color.White

/**
 * VIEW — Pantalla completa de la calculadora.
 * Solo observa el estado del ViewModel y reenvía eventos.
 */
@Composable
fun CalculadoraScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculadoraViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Botón 1 de la fila superior alterna dinámicamente
    val botonLimpiar = if (state.displayEsInicial) "AC" else "⌫"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Negro)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // ── Display ──────────────────────────────────────────────
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

        // ── Teclado ───────────────────────────────────────────────
        // Fila 1: AC/⌫ · +/- · % · ÷
        // Fila 5: ... · 0 · . · =   (... reemplaza al +/-)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Panel de Convertir (agregado)
            AnimatedVisibility(visible = state.isPanelVisible) {
                Column {
                    ConvertirPanel(
                        isEnabled = state.isConvertirEnabled,
                        onToggle = { viewModel.toggleConvertir() }
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Espaciado visual
                }
            }

            // Handler unificado para cualquier tipo de tecla
            val onKeyAction: (Any) -> Unit = { key ->
                when (key) {
                    is String -> viewModel.onKey(key)
                    Icons.Default.Calculate -> viewModel.togglePanel()
                }
            }

            CalcRow(listOf(botonLimpiar, "+/-", "%", "÷"), onKeyAction)
            CalcRow(listOf("7", "8", "9", "×"), onKeyAction)
            CalcRow(listOf("4", "5", "6", "-"), onKeyAction)
            CalcRow(listOf("1", "2", "3", "+"), onKeyAction)
            // Reemplazo de "..." por el icono de calculadora
            CalcRow(listOf(Icons.Default.Calculate, "0", ".", "="), onKeyAction)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculadoraScreen() {
    CalculadoraTipoIPhoneTheme { CalculadoraScreen() }
}
