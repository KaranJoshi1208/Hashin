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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.karan.hashin.R
import com.karan.hashin.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Passkey(
    viewModel: HomeViewModel,
    doEdit: Boolean,
    navController: NavController,
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${if (doEdit) "Edit" else "Add"} Passkey") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            Text(
                text = "${if (doEdit) "Update" else "Store"} your credentials securely",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
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
                            serviceError = false
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
                        placeholder = { Text("e.g. GitHub") },
                        leadingIcon = { Icon(Icons.Default.Web, contentDescription = "Website name") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = serviceError,
                        supportingText = if (serviceError) {
                            { Text("Service name is required") }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
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
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    OutlinedTextField(
                        value = if (doEdit) "••••••••" else pass,
                        onValueChange = {
                            pass = it
                            passError = false
                        },
                        enabled = !doEdit,
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                        trailingIcon = {
                            val image =
                                if (isPasswordVisible) R.drawable.visibility else R.drawable.visibility_off

                            Icon(
                                painter = painterResource(id = image),
                                contentDescription = "Toggle password visibility",
                                modifier = Modifier
                                    .clickable {
                                        isPasswordVisible = !isPasswordVisible
                                    }
                            )
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = passError && !doEdit,
                        supportingText = if (passError && !doEdit) {
                            { Text("Password is required") }
                        } else null,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            OutlinedTextField(
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
                label = { Text("Description (optional)") },
                placeholder = { Text("Add a description...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary
                )
            )

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
                    serviceError = service.isBlank()
                    if (!doEdit) passError = pass.isBlank()

                    if (!serviceError && (!passError || doEdit)) {
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
                            viewModel.addPassKey(service, username, pass, desc, label)
                        }
                        // reset input fields
                        service = ""
                        username = ""
                        pass = ""
                        desc = ""
                        label = ""
                    }
                },
                enabled = if (doEdit) isChanged && !viewModel.processing else !viewModel.processing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (viewModel.processing) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 8.dp)
                    )
                } else {
                    Text(
                        text = if (doEdit) "Update" else "Add",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
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
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
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
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = label,
                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                    )
                }
            }
        }
    }
}