package com.example.calculadoratipoiphone.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.calculadoratipoiphone.viewmodel.CalculadoraViewModel

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: CalculadoraViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Top Bar con botón de regreso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Text(
                text = "Historial",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        HorizontalDivider(color = Color.DarkGray)

        if (state.historial.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay operaciones recientes",
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                reverseLayout = true // Para ver los más recientes abajo o usar reverseLayout=false y ver recientes arriba si la lista está en orden de insercion.
                // En viewModel yo hacía `historial + entry`, así que el último es el más reciente (al final de la lista).
                // Si quiero ver recientes arriba, reverseLayout=true mostrará el final de la lista arriba.
                // Espera, reverseLayout=true muestra el item 0 ABAJO y el item N ARRIBA.
                // Si agrego al final, el mas nuevo es el utimo.
                // LazyColumn empieza desde arriba. Si reverse=true, empieza desde abajo.
                // Probemos normal y vemos.
            ) {
                items(state.historial.reversed()) { operation ->
                    Text(
                        text = operation,
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        textAlign = TextAlign.End
                    )
                    HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

