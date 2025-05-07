package com.karan.hashin.components

import androidx.compose.ui.graphics.Color
import com.karan.hashin.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun BottomAppBar(modifier: Modifier = Modifier) {

    Card(
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(top = 8.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxSize()
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.shield),
                contentDescription = "Vault",
                modifier = Modifier
                    .padding(start = 32.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Green.copy(alpha = 0.5f))
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.add),
                    contentDescription = "Vault",
                    modifier = Modifier
                        .size(40.dp)
                        
                )
            }

            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                contentDescription = "Vault",
                modifier = Modifier
                    .padding(end = 32.dp)
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviewBottomBar() {
    HashinTheme {
        BottomAppBar()
    }
}