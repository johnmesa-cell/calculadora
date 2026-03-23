package com.example.calculadoratipoiphone.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoratipoiphone.view.CalculadoraScreen
import com.example.calculadoratipoiphone.view.HistoryScreen
import com.example.calculadoratipoiphone.viewmodel.CalculadoraViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.calculadoratipoiphone.view.components.ConvertirPanel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // CRÍTICO: Instancia única del ViewModel
    val viewModel: CalculadoraViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "calculadora"
    ) {
        composable("calculadora") {
            CalculadoraScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("historial") {
            HistoryScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("conversor") {
            // Ruta para el conversor de unidades y funciones científicas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                ConvertirPanel(
                    isEnabled = true, // Siempre habilitado en esta pantalla
                    onToggle = { /* No-op aquí, o podria ser para activar modo científico vs unidades */ },
                    onBack = { navController.popBackStack() },
                    viewModel = viewModel
                )
            }
        }
    }
}
