package com.karan.hashin.components

import com.karan.hashin.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun TopAppBar(modifier: Modifier = Modifier) {

    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .padding(bottom = 8.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
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

            Spacer(Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.search),
                contentDescription = "Search for passkeys",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BarPreview() {
    HashinTheme {
        TopAppBar()
    }

}