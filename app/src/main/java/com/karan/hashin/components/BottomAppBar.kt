package com.karan.hashin.components

import android.content.res.Configuration
import com.karan.hashin.ui.theme.iconTintDark
import androidx.compose.ui.graphics.Color
import com.karan.hashin.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.ui.theme.PurpleA700
import com.karan.hashin.ui.theme.iconTintLight

@Composable
fun BottomAppBar(
    toVault: () -> Unit = {},
    toPassKey: () -> Unit = {},
    toSetting: () -> Unit = {},
    selection: Int,
    modifier: Modifier = Modifier
) {
    val iconTint = iconTintDark
    Card(
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(80.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector =Icons.Default.Key ,
                contentDescription = "Vault",
                tint = if(selection == 1) iconTint else MaterialTheme.colorScheme.primary ,
                modifier = Modifier
                    .size(32.dp)
                    .semantics { Role.Button }
                    .clickable(true) {
                        toVault()
                    }
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 2.dp,
                        color = if(selection == 2) iconTint else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(percent = 33)
                    )
//
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "PassKey",
                    tint = if(selection == 2) iconTint else MaterialTheme.colorScheme.primary ,
                    modifier = Modifier
                        .size(36.dp)
                        .semantics { Role.Button }
                        .clickable(true) {
                            toPassKey()
                        }
                )
            }

            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Settings",
                tint = if(selection == 3) iconTint else MaterialTheme.colorScheme.primary ,
                modifier = Modifier
                    .size(32.dp)
                    .semantics { Role.Button }
                    .clickable(true) {
                        toSetting()
                    }
            )
        }
    }
}

@Preview(
    name = "BottomAppBar Preview Light",
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
private fun p1() {
    HashinTheme(darkTheme = true, dynamicColor = false) {
        BottomAppBar({}, {}, {}, 2)
    }
}