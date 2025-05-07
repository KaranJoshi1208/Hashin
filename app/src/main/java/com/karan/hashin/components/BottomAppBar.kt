package com.karan.hashin.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun BottomAppBar(modifier: Modifier = Modifier) {

    Card(
        shape = RectangleShape,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
        ) {

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewBottomBar() {
    HashinTheme {
        BottomAppBar()
    }
}