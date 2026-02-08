package com.example.appcorpas.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.appcorpas.viewmodel.AgendaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: AgendaViewModel,
    userId: String?,
    onBack: () -> Unit
) {
    val context = LocalContext.current //para lanzar Intents
    val listaUsuarios by viewModel.contactos.collectAsState()

    val usuario = remember(listaUsuarios, userId) {
        listaUsuarios.find { it.id == userId }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del Contacto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (usuario != null) {
                        //boton compartir
                        IconButton(onClick = {
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Mira este contacto: ${usuario.nombre} - ${usuario.telefono}")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Compartir contacto"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Compartir")
                        }

                        //Boton borrar
                        IconButton(onClick = {
                            // Borramos el contacto usando la función que creamos antes
                            viewModel.borrarContacto(usuario.id)
                            // Volvemos atrás inmediatamente
                            onBack()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Borrar contacto",
                                tint = MaterialTheme.colorScheme.error // Color ROJO de alerta
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (usuario != null) {
                AsyncImage(
                    model = usuario.foto,
                    contentDescription = "Foto de ${usuario.nombre}",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = usuario.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // Al hacer clic llama por telefono
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${usuario.telefono}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Llamar a ${usuario.telefono}")
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                        FilaDato(icon = Icons.Default.Email, titulo = "Email", valor = usuario.id)
                    }
                }

            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun FilaDato(icon: ImageVector, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = titulo, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = valor, style = MaterialTheme.typography.bodyLarge)
        }
    }
}