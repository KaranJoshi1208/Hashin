package com.karan.hashin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.karan.hashin.screens.auth.AuthScreen
import com.karan.hashin.screens.home.HomeScreen
import com.karan.hashin.screens.splash.Splash
import com.karan.hashin.viewmodel.AuthViewModel
import com.karan.hashin.viewmodel.HomeViewModel
import com.karan.hashin.viewmodel.SplashViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route,
        modifier = Modifier
    ) {

        composable(
            route = Screens.Splash.route
        ) {
            val splashViewModel: SplashViewModel = viewModel(it)
            Splash(splashViewModel, navController, Modifier)
        }

        composable(
            route = Screens.Auth.route
        ) {
            val authViewModel: AuthViewModel = viewModel(it)
            AuthScreen(authViewModel, navController, Modifier)
        }

        composable(
            route = Screens.Home.route
        ) {
            val vm : HomeViewModel = viewModel(it)
            HomeScreen(vm, navController /*parent*/, /*navController,*/ Modifier)
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.getViewModel(navController: NavController) : T {
    val parentRoute = destination.parent?.route ?: return viewModel<T>()
    val backStack = remember(this) {
        navController.getBackStackEntry(parentRoute)
    }
    return viewModel(backStack)
}