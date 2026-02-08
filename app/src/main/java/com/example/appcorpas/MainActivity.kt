package com.example.appcorpas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels // Necesario para "by viewModels()"
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appcorpas.navigation.AppNavigation
import com.example.appcorpas.networkhelper.NetworkHelper
import com.example.appcorpas.viewmodel.AgendaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Inyectamos el ViewModel de forma segura con Hilt
    private val viewModel: AgendaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Usamos MaterialTheme estándar para evitar errores de importación
            MaterialTheme {

                // Detector de red
                val context = LocalContext.current
                val networkHelper = remember { NetworkHelper(context) }
                val isConnected by networkHelper.observeNetworkStatus.collectAsState(initial = true)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {

                        AppNavigation(viewModel)

                        AnimatedVisibility(
                            visible = !isConnected,
                            enter = expandVertically(),
                            exit = shrinkVertically(),
                            modifier = Modifier.align(Alignment.BottomCenter)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Red)
                                    .padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "⚠️ SIN CONEXIÓN A INTERNET",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}