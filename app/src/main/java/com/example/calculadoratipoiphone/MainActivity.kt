package com.example.calculadoratipoiphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.calculadoratipoiphone.navigation.AppNavigation
import com.example.calculadoratipoiphone.ui.theme.CalculadoraTipoIPhoneTheme

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
                    Box(modifier = Modifier.padding(padding)) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
