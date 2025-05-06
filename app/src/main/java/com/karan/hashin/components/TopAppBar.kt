package com.karan.hashin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun TopAppBar(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 48.dp)
            .fillMaxWidth()
            .height(64.dp)

    ) {
        Text(
            text = "#",
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BarPreview() {
    HashinTheme {
        TopAppBar()
    }
    
}