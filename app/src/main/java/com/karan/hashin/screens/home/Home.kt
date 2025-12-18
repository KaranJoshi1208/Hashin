package com.karan.hashin.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.karan.hashin.components.BottomAppBar
import com.karan.hashin.navigation.Screens
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val innerNav = rememberNavController()
    var selection by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                toVault = {
                    selection = 1
                    innerNav.navigate(Screens.HomeGraph.Vault.route) {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                toPassKey = {
                    selection = 2
                    innerNav.navigate(Screens.HomeGraph.Passkey.generateRoute(isEdit = false))
                    {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                toSetting = {
                    selection = 3
                    innerNav.navigate(Screens.HomeGraph.Setting.route)
                    {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                selection = selection,
                modifier = Modifier
            )
        },
        modifier = modifier
            .navigationBarsPadding()
            .statusBarsPadding()
    ) { pd ->
        NavHost(
            navController = innerNav,
            startDestination = Screens.HomeGraph.Vault.route,
            modifier = Modifier
                .padding(pd)
        ) {
            composable(
                route = Screens.HomeGraph.Vault.route
            ) {
                Vault(viewModel, innerNav)
            }

            composable(
                route = Screens.HomeGraph.Passkey.route,
                arguments = listOf(
                    navArgument("isEdit") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) {
                val isEdit = it.arguments?.getBoolean("isEdit") ?: false
                Passkey(viewModel, isEdit, innerNav)
            }

            composable(
                route = Screens.HomeGraph.Setting.route
            ) {
                Settings(viewModel, innerNav)
            }

            composable(
                route = Screens.HomeGraph.Detail.route
            ) {
                PassKeyDetail(viewModel, innerNav)
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    HashinTheme {
        HomeScreen(viewModel<HomeViewModel>())
    }
}