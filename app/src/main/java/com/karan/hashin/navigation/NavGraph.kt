package com.karan.hashin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karan.hashin.screens.AuthScreen
import com.karan.hashin.viewmodel.AuthViewModel


@Composable
fun NavGraph(
    authViewModel : AuthViewModel,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Auth.name           // start should be Splash
    ) {

        composable(
            route = Screens.Auth.name
        ) {
            AuthScreen(modifier = Modifier)
        }

        composable(
            route = Screens.Home.name
        ) {

        }
    }
}