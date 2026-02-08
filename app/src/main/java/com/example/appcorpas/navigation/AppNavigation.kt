package com.example.appcorpas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcorpas.ui.screens.DetalleScreen
import com.example.appcorpas.ui.screens.ListaUsuariosScreen
import com.example.appcorpas.ui.screens.LoginScreen
import com.example.appcorpas.viewmodel.AgendaViewModel

@Composable
fun AppNavigation(viewModel: AgendaViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("lista_usuarios") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("lista_usuarios") {
            ListaUsuariosScreen(
                viewModel = viewModel,
                onUsuarioClick = { usuario ->
                    navController.navigate("detalle_usuario/${usuario.id}")
                }
            )
        }

        composable(
            route = "detalle_usuario/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            DetalleScreen(
                viewModel = viewModel,
                userId = userId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}