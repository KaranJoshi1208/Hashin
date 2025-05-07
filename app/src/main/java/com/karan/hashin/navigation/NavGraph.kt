package com.karan.hashin.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karan.hashin.screens.AuthScreen
import com.karan.hashin.screens.HomeScreen
import com.karan.hashin.viewmodel.AuthViewModel


@Composable
fun NavGraph(
    innerPadding : PaddingValues,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Auth.name,          // start should be Splash
        modifier = Modifier
            .padding(innerPadding)
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