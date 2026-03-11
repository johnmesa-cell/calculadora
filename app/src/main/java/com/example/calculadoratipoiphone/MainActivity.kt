package com.example.calculadoratipoiphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoratipoiphone.ui.theme.CalculadoraTipoIPhoneTheme
import com.example.calculadoratipoiphone.view.CalculadoraScreen
import com.example.calculadoratipoiphone.viewmodel.CalculadoraViewModel

/**
 * VIEW — Punto de entrada de Android.
 * Única responsabilidad: configurar el tema y lanzar CalculadoraScreen.
 * No contiene ningún Composable de UI ni lógica de negocio.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTipoIPhoneTheme {
                Scaffold(
                    containerColor = Color.Black,
                    modifier = Modifier.fillMaxSize()
                ) { padding ->
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
