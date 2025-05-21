package com.karan.hashin.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karan.hashin.navigation.Screens
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.HomeViewModel

@Composable
fun Search(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun PreviewSearch() {
    HashinTheme {
        Search(viewModel<HomeViewModel>())
    }
}