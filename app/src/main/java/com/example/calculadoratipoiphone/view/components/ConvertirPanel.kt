package com.example.calculadoratipoiphone.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
private val DividerColor = Color(0xFF2C2C2E)

@Composable
fun ConvertirPanel(
    isEnabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(PanelBackground, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // Fila 1: Icono + Texto "Área"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Usamos Settings como placeholder de regla/balanza si no hay icono específico
            Icon(
                imageVector = Icons.Default.Settings, 
                contentDescription = "Área",
                tint = VioletText,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Área",
                color = VioletText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        HorizontalDivider(color = DividerColor, thickness = 1.dp)

        // Fila 2: Texto "Convertir" + Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Convertir",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Switch(
                checked = isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = GreenSwitch,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.DarkGray,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }
    }
}

