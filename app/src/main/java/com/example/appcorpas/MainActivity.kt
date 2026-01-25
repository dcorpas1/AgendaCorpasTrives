package com.example.appcorpas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues // IMPORTANTE
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // IMPORTANTE
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.compose.material.icons.filled.Refresh

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val agendaViewModel: AgendaViewModel by viewModels()
        setContent {
            MaterialTheme {
                AppNavigation(agendaViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: AgendaViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaUsuariosScreen(viewModel) { usuario ->
                val fotoCodificada = URLEncoder.encode(usuario.picture.large, StandardCharsets.UTF_8.toString())
                navController.navigate("detalle/${usuario.name.first} ${usuario.name.last}/${usuario.cell}/$fotoCodificada")
            }
        }
        composable(
            route = "detalle/{nombre}/{telefono}/{fotoUrl}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("telefono") { type = NavType.StringType },
                navArgument("fotoUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            val fotoUrl = backStackEntry.arguments?.getString("fotoUrl") ?: ""

            DetalleScreen(nombre, telefono, fotoUrl) {
                navController.popBackStack()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaUsuariosScreen(viewModel: AgendaViewModel, onUserClick: (UsuarioApi) -> Unit) {
    val usuarios by viewModel.usuarios.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agenda Corpas y Trives") },
                //BOTÓN RECARGAR
                actions = {
                    IconButton(onClick = { viewModel.cargarUsuarios() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Recargar datos"
                        )
                    }
                }
            )
        }
    ) { padding: PaddingValues ->
        if (usuarios.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(usuarios) { usuario: UsuarioApi ->
                    ItemUsuario(usuario, onUserClick)
                }
            }
        }
    }
}
@Composable
fun ItemUsuario(usuario: UsuarioApi, onClick: (UsuarioApi) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(usuario) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = usuario.picture.large,
                contentDescription = "Foto perfil",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "${usuario.name.first} ${usuario.name.last}", style = MaterialTheme.typography.titleMedium)
                Text(text = usuario.email, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(nombre: String, telefono: String, fotoUrl: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding: PaddingValues -> // <--- ESTA ERA LA LÍNEA 129 QUE DABA EL ERROR
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = fotoUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = nombre, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "📞 Teléfono: $telefono")
                    Text(text = "📧 Email: (Ver en lista)")
                }
            }
        }
    }
}