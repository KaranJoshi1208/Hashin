package com.karan.hashin.components

import com.karan.hashin.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun Element(modifier: Modifier = Modifier) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.people),
                contentDescription = "Element Icon Image",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(52.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = "Netflix",
                    fontSize = 22.sp,
                    color = Color.Red
                )

                Text(
                    text = "Bolt",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.Red)
            ) {
                Text(
                    text = "E",
                    color = Color.White,
                    fontSize = 36.sp,
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewElement() {
    HashinTheme {
        Element(Modifier.padding(top = 144.dp))
    }
}