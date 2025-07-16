package com.karan.hashin.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import com.karan.hashin.ui.theme.iconTintDark
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.karan.hashin.ui.theme.HashinTheme

@Composable
fun NavigationBar(
    toVault: () -> Unit = {},
    toPassKey: () -> Unit = {},
    toSetting: () -> Unit = {},
    selection: Int,
    modifier: Modifier = Modifier
) {
    val iconTint by remember { mutableStateOf(iconTintDark) }
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
            .wrapContentHeight()
//            .height(88.dp)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxWidth()
//                .navigationBarsPadding()
                .padding(top = 8.dp)
                .height(64.dp)

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
        Spacer(Modifier.navigationBarsPadding())
    }
}

@Preview(
    name = "BottomAppBar Preview Light",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun P1() {
    HashinTheme(darkTheme = true, dynamicColor = false) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavigationBar({}, {}, {}, 2)
        }
    }
}