package com.example.pokemart // Asegúrate de que coincida con tu paquete actual
import PokemartScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Usamos el MaterialTheme genérico por defecto
            MaterialTheme {
                // Surface se encarga de pintar el fondo estándar (blanco o gris claro)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llamada directa a la pantalla del examen
                    PokemartScreen()
                }
            }
        }
    }
}

