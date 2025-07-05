package com.karan.hashin.screens.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karan.hashin.components.Element
import com.karan.hashin.viewmodel.HomeViewModel

@Composable
fun Vault(
    viewModel: HomeViewModel,
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

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        if (viewModel.isFetchingData) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "Loading...", fontSize = 30.sp)
            }
        } else {
            var data = viewModel.passkeys.also {
                Log.d("#ined", "data: ${it.toList()}")
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(count = data.size, key = { it }) { i ->
                    Element(data[i]) {
//                    TODO("Define Card onClick")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun VaultPreview() {
    Vault(viewModel<HomeViewModel>())
}