package com.example.calculadoratipoiphone.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.calculadoratipoiphone.viewmodel.CalculadoraViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores específicos del panel
private val PanelBackground = Color(0xFF1C1C1E)
private val VioletText = Color(0xFFD0BCFF)
private val GreenSwitch = Color(0xFF4CD964)

//@Preview
@Composable
fun ConvertirPanel(
    isEnabled: Boolean,
    onToggle: () -> Unit,
    onBack: (() -> Unit)? = null, // Opcional, para usarlo como NavScreen
    viewModel: CalculadoraViewModel? = null // Opcional para poder llamar funciones
) {
    val state = viewModel?.uiState?.collectAsState()?.value
    val isDegrees = state?.isDegrees ?: true

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PanelBackground, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Cabecera con botón Back si existe
        if (onBack != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Avanzado",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Fila 1: Icono y Texto "Área" (o lo que sea la unidad actual)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.SquareFoot, // Icono similar a regla
                contentDescription = null,
                tint = VioletText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Funciones Científicas",
                color = VioletText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(12.dp))

        // Fila 2: "Grados" en lugar de "Convertir" para este contexto matemático
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isDegrees) "Grados (DEG)" else "Radianes (RAD)",
                color = Color.White,
                fontSize = 17.sp
            )
            Switch(
                checked = isDegrees,
                onCheckedChange = { viewModel?.onKey("deg/rad") },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GreenSwitch,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.DarkGray
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Grid de Botones Científicos
        if (viewModel != null) {
            val botones = listOf(
                "sin", "cos", "tan",
                "ln", "log", "sqrt",
                "x²", "x^y", "π"
            )

            Column {
                botones.chunked(3).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        fila.forEach { label ->
                            Button(
                                onClick = { 
                                    viewModel.onKey(label)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3A3C)),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.size(width = 80.dp, height = 60.dp)
                            ) {
                                Text(
                                    text = label, 
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
