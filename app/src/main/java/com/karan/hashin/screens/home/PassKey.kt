package com.karan.hashin.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.HomeViewModel
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale

@Composable
fun Passkey(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var userName by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(top = 144.dp)        // just for dev

    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName += it },
                    label = { Text("User Name", color = Color.Gray.copy(alpha = 03f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass += it },
                    label = { Text("Password", color = Color.Gray.copy(alpha = 03f)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
//                containerColor = Color(0xFFC3CBFF),
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp)
                .padding(horizontal = 12.dp)
        ) {
            BasicTextField(
                value = desc,
                onValueChange = { desc += it },
                decorationBox = { innerTextField ->
                    if (desc.isEmpty()) {
                        Text(
                            text = "Description",
                            style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }

        LabelSelector(
            listOf("Enter", "Personal", "Work", "Business")
        ) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun LabelSelector(
    labels: List<String>,
    modifier: Modifier = Modifier,
    onLabelSelected: (String) -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        itemsIndexed(labels) { index, label ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) Color(0xFF6200EE) else Color.LightGray
                    )
                    .clickable {
                        selectedIndex = index
                        onLabelSelected(label)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = label,
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPassKey() {
    HashinTheme {
        Passkey(viewModel<HomeViewModel>())
    }

}