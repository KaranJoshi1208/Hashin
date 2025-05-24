package com.karan.hashin.screens.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.karan.hashin.components.Element
import com.karan.hashin.viewmodel.HomeViewModel

@Composable
fun Vault(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    var data = viewModel.passkeys.also {
        Log.d("#ined", it.toString())
    }
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
    ) {

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(count = data.size, key = { it }) {i ->
                Element(data[i]) {
//                    TODO("Define Card onClick")
                }
            }
        }
    }
}