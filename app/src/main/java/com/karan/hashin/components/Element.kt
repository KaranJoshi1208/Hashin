package com.karan.hashin.components

import android.content.res.Configuration
import com.karan.hashin.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karan.hashin.model.local.PassKey
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun Element(
    passKey: PassKey,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable(true) { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon (
                imageVector = ImageVector.vectorResource(id = R.drawable.people),
                contentDescription = "Element Icon Image",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(36.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = passKey.service.ifEmpty { "Website" },
                    fontSize = 20.sp,
                    color = getColorForLabel(passKey.label)
                )

                Text(
                    text = passKey.userName,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 2.dp)
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
                    .background(getColorForLabel(passKey.label))
            ) {
                Text(
                    text = passKey.label.firstOrNull()?.uppercase() ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.W300,
                    fontSize = 36.sp,
                )
            }
        }
    }
}

@Preview(
    name = "Dark Theme",
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Theme",
    showBackground = true,
    showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun PreviewElement() {
    HashinTheme(dynamicColor = false) {
        Element(PassKey(
            service = "Github",
            userName = "KaranJoshi1208",
            desc = "My passkey",
            label = "Work"
        ),
            modifier = Modifier,
            onClick = {}
        )
    }
}

private fun getColorForLabel(label: String): Color {
    return when (label.lowercase()) {
        "personal" -> Color(0xFF2196F3) // Blue
        "work" -> Color(0xFF4CAF50) // Green
        "business" -> Color(0xFFFF9800) // Orange
        "social" -> Color(0xFFE91E63) // Pink
        "other" -> Color(0xFF9C27B0) // Purple
        else -> Color(0xFFF44336) // Red (default)
    }
}