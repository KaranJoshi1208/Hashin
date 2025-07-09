package com.karan.hashin.screens.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.components.Element
import com.karan.hashin.components.TopAppBar
import com.karan.hashin.navigation.Screens
import com.karan.hashin.viewmodel.HomeViewModel

@Composable
fun Vault(
    viewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    var isExiting = remember { mutableStateOf(false) }

    BackHandler {
        if (!isExiting.value) {
            isExiting.value = true
            dispatcher?.onBackPressed()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top App Bar
//        TopAppBar(
//            onSearch = {
//                // TODO: Implement search functionality
//            }
//        )

        // Content
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (viewModel.isFetchingData) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Loading...", fontSize = 30.sp)
                }
            } else {
                val data = viewModel.passkeys.also {
                    Log.d("#ined", "data: ${it.toList()}")
                }

                if (data.isEmpty()) {
                    // Empty state
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No passkeys yet",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Tap the + button to add your first passkey",
                            fontSize = 16.sp,
                            color = androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        items(
                            items = data,
                            key = { it.id }
                        ) { passKey ->
                            Element(passKey) {
                                viewModel.userSelected = data.indexOf(passKey)
                                navController.navigate(Screens.HomeGraph.Detail.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun VaultPreview() {
    Vault(viewModel<HomeViewModel>(), rememberNavController())
}