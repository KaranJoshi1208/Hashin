package com.karan.hashin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karan.hashin.screens.AuthScreen
import com.karan.hashin.screens.HomeScreen
import com.karan.hashin.viewmodel.AuthViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Auth.name           // start should be Splash
    ) {

        composable(
            route = Screens.Auth.name
        ) {
            val authViewModel : AuthViewModel = viewModel(it)
            AuthScreen(authViewModel, navController, Modifier)
        }

        composable(
            route = Screens.Home.name
        ) {
            val homeViewModel : AuthViewModel = viewModel(it)
            HomeScreen()
        }
    }
}