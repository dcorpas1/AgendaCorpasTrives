package com.example.appcorpas.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appcorpas.data.local.entity.UsuarioEntity
import com.example.appcorpas.viewmodel.AgendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaUsuariosScreen(
    viewModel: AgendaViewModel,
    onUsuarioClick: (UsuarioEntity) -> Unit
) {
    // Recogemos la lista del ViewModel
    val usuarios by viewModel.contactos.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agenda de Corpas y Trives") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.cargarDatos(forzar = true)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Recargar contactos"
                )
            }
        }
    ) { padding ->

        if (usuarios.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando contactos...")
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(usuarios) { usuario ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onUsuarioClick(usuario) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            // Aquí iría la foto (AsyncImage) si quieres ponerla
                            Column {
                                Text(
                                    text = usuario.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = usuario.telefono,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}