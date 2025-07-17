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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karan.hashin.R
import com.karan.hashin.ui.theme.Unfocused
import com.karan.hashin.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun Passkey(
    viewModel: HomeViewModel,
    doEdit: Boolean,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    var service by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }

    // Flags
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isChanged by remember { mutableStateOf(false) }
    var serviceError by remember { mutableStateOf(false) }
    var passError by remember { mutableStateOf(false) }

    val passkey = remember {
        if (doEdit) viewModel.passkeys[viewModel.userSelected].also {
            service = it.service
            username = it.userName
            desc = it.desc
            label = it.label
        }
        else null
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        Text(
            text = "${if (doEdit) "Update" else "Add"} Passkey ",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${if (doEdit) "Update" else "Store"} your credentials securely",
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
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
                    value = service,
                    onValueChange = {
                        service = it
                        isChanged = passkey?.let {
                            viewModel.detectChange(
                                Pair(service, passkey.service),
                                Pair(username, passkey.userName),
                                Pair(label, passkey.label),
                                Pair(desc, passkey.desc)
                            )
                        } ?: false
                    },
                    label = { Text("Service") },
                    placeholder = { Text("eg. Github", color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)) },
                    leadingIcon = { Icon(Icons.Default.Web, contentDescription = "Website name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Unfocused,
                        unfocusedLeadingIconColor = Unfocused,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        isChanged = passkey?.let {
                            viewModel.detectChange(
                                Pair(service, passkey.service),
                                Pair(username, passkey.userName),
                                Pair(label, passkey.label),
                                Pair(desc, passkey.desc)
                            )
                        } ?: false
                    },
                    label = { Text("Username") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Username") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Unfocused,
                        unfocusedLeadingIconColor = Unfocused,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )

                OutlinedTextField(
                    value = if (doEdit) "••••••••" else pass,
                    onValueChange = { pass = it },
                    enabled = !doEdit,
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
                        unfocusedBorderColor = Unfocused,
                        unfocusedLeadingIconColor = Unfocused,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        focusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
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
                onValueChange = {
                    desc = it
                    isChanged = passkey?.let {
                        viewModel.detectChange(
                            Pair(service, passkey.service),
                            Pair(username, passkey.userName),
                            Pair(label, passkey.label),
                            Pair(desc, passkey.desc)
                        )
                    } ?: false
                },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp, horizontal = 20.dp)
                    ) {
                        if (desc.isEmpty()) {
                            Text(
                                text = "Add a description...",
                                style = TextStyle(
                                    color = Unfocused,
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
            isChanged = passkey?.let {
                viewModel.detectChange(
                    Pair(service, passkey.service),
                    Pair(username, passkey.userName),
                    Pair(label, passkey.label),
                    Pair(desc, passkey.desc)
                )
            } ?: false
        }

        Spacer(modifier = Modifier.weight(1f))

        // Add/Update Button
        Button(
            onClick = {
                if (service.isBlank()) serviceError = true
                if (pass.isBlank()) passError = true

                if (!serviceError) {
                    if (doEdit) {
                        passkey?.let {
                            val newPassKey = it.copy(
                                service = service,
                                userName = username,
                                desc = desc,
                                label = label
                            )
                            viewModel.updatePasskey(newPassKey)
                        } ?: Toast.makeText(
                            context,
                            "Update Error (passkey == null)",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (!passError) {
                            viewModel.addPassKey(service, username, pass, desc, label)
                        }
                    }
                    // reset input fields
                    service = ""
                    username = ""
                    pass = ""
                    desc = ""
                    label = ""
                }
            },
            enabled = if (doEdit) isChanged else true,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (viewModel.processing) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 8.dp)
                )
            } else {
                Text(
                    text = if (doEdit) "Update" else "Add",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
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