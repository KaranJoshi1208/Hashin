package com.karan.hashin.screens.home

import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.karan.hashin.R
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Passkey(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var webSite by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {

        Text(
            text = "Add New Passkey",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Text(
            text = "Store your credentials securely",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = webSite,
                    onValueChange = { webSite = it },
                    label = { Text("Website") },
                    leadingIcon = { Icon(Icons.Default.Web, contentDescription = "Website name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6200EE),
                        focusedLabelColor = Color(0xFF6200EE)
                    )
                )
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6200EE),
                        focusedLabelColor = Color(0xFF6200EE)
                    )
                )

                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    trailingIcon = {
                        val image =
                            if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off

                        Icon(
                            painter = painterResource(id = image),
                            contentDescription = "Visibility state of password",
                            modifier = Modifier
                                .clickable {
                                    isPasswordVisible = !isPasswordVisible
                                }
                        )
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6200EE),
                        focusedLabelColor = Color(0xFF6200EE)
                    )
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            BasicTextField(
                value = desc,
                onValueChange = { desc = it },
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        if (desc.isEmpty()) {
                            Text(
                                text = "Add a description...",
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        LabelSelector(
            listOf("Personal", "Work", "Business", "Social", "Other")
        ) {
            label = it
        }

        Spacer(modifier = Modifier.weight(1f))

        // Save Button
        Button(
            onClick = {
                viewModel.addPassKey(webSite, userName, pass, desc, label)
                // reset input fields
                userName = ""
                pass = ""
                desc = ""
                label = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Save Passkey",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun LabelSelector(
    labels: List<String>,
    modifier: Modifier = Modifier,
    onLabelSelected: (String) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val animatedProgress = remember { Animatable(0f) }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        itemsIndexed(labels) { index, label ->
            val isSelected = index == selectedIndex
            val scale = animateFloatAsState(
                targetValue = if (isSelected) 1.1f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale"
            )

            val elevation = animateDpAsState(
                targetValue = if (isSelected) 8.dp else 2.dp,
                animationSpec = tween(durationMillis = 200),
                label = "elevation"
            )

            Card(
                modifier = Modifier
                    .size(width = 144.dp, height = 72.dp)
                    .clickable {
                        selectedIndex = index
                        onLabelSelected(label)
                        scope.launch {
                            animatedProgress.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }
                    .scale(scale.value),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFF9C27B0) else Color(0xFFF0F0F0)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = elevation.value
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = label,
                        color = if (isSelected) Color.White else Color(0xFF666666),
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPassKey() {
    HashinTheme {
        Passkey(viewModel<HomeViewModel>())
//        Passkey()
    }
}