package com.karan.hashin.screens

import android.text.Layout
import android.widget.GridLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.karan.hashin.ui.theme.HashinTheme


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(

            ) {

            }
        }
//        Text(
//            text = "Home Screen",
//            fontSize = 60.sp,
//        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomePreview() {
    HashinTheme {
        HomeScreen()
    }
}