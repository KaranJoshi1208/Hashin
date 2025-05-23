package com.karan.hashin.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.components.BottomAppBar
import com.karan.hashin.components.TopAppBar
import com.karan.hashin.navigation.Screens
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val innerNav = rememberNavController()
//    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
//    var isExiting = remember { mutableStateOf(false) }
//
//    BackHandler {
//        if (!isExiting.value) {
//            isExiting.value = true
//            dispatcher?.onBackPressed()
//        }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                onSearch = {
                    innerNav.navigate(Screens.HomeGraph.Search.route)
                },
                modifier = Modifier
            )
        },
        bottomBar = {
            BottomAppBar(
                toVault = {
                    innerNav.navigate(Screens.HomeGraph.Vault.route) {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                toPassKey = {
                    innerNav.navigate(Screens.HomeGraph.Passkey.route)
                    {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                toSetting = {
                    innerNav.navigate(Screens.HomeGraph.Setting.route)
                    {
                        popUpTo(0) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
            )
        },
        modifier = Modifier
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
                Vault(viewModel)
            }

            composable(
                route = Screens.HomeGraph.Passkey.route
            ) {
                Passkey(viewModel)
            }

            composable(
                route = Screens.HomeGraph.Setting.route
            ) {
                Settings(viewModel)
            }

            composable(
                route = Screens.HomeGraph.Search.route
            ) {
                Search(viewModel)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomePreview() {
    HashinTheme {
        HomeScreen(viewModel<HomeViewModel>(), rememberNavController())
    }
}