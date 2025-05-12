package com.karan.hashin.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.karan.hashin.components.Element
import com.karan.hashin.viewmodel.HomeViewModel

@Composable
fun Vault(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
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
            val data = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

            items(count = data.size, key = { it }) {
                Element()
            }
        }
    }
}