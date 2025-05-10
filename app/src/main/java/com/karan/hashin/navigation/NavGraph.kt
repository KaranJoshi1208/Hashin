package com.karan.hashin.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.karan.hashin.screens.AuthScreen
import com.karan.hashin.screens.HomeScreen
import com.karan.hashin.screens.Splash
import com.karan.hashin.viewmodel.AuthViewModel
import com.karan.hashin.viewmodel.SplashViewModel


@Composable
fun NavGraph(
    innerPadding : PaddingValues,
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route,          // start should be Splash
        modifier = Modifier
            .padding(innerPadding)
            .navigationBarsPadding()
    ) {

        composable(
            route = Screens.Splash.route
        ) {
            val vm : SplashViewModel = viewModel(it)
            Splash(vm, navController, Modifier)
        }

        composable(
            route = Screens.Auth.route
        ) {
            val authViewModel : AuthViewModel = viewModel(it)
            AuthScreen(authViewModel, navController, innerPadding, Modifier)
        }

        navigation(
            startDestination = Screens.HomeGraph.Vault.route,
            route = Screens.Home.route
        ) {

            // this logic only tells when to change state for navigation, think about it !!!

            composable(
                route = Screens.HomeGraph.Vault.route
            ) {
                HomeScreen()
            }

        }

        composable(
            route = Screens.HomeGraph.Vault.route
        ) {
            val homeViewModel : AuthViewModel = viewModel(it)
            HomeScreen()
        }
    }
}