package com.karan.hashin.screens.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Icon
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.karan.hashin.navigation.Screens
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.HomeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassKeyDetail(
    viewModel: HomeViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val passkey = if (viewModel.userSelected > -1) viewModel.passkeys[viewModel.userSelected] else return

    var showSave by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }


    var isKeyVisible by remember { mutableStateOf(false) }
    var showCopiedToast by remember { mutableStateOf(false) }

    var newKey by remember { mutableStateOf(passkey.pass) }
//    var key by remember { mutableStateOf(passkey.pass) }

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    DeletePasskeyDialog(
        visible = deleteDialog,
        onConfirmDelete = {
            viewModel.deletePasskey(passkey.id)
            navController.navigate(Screens.HomeGraph.Vault.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
            viewModel.passkeys.remove(passkey).also {
                viewModel.userSelected = -1
                Log.d("#ined", "data =  ${viewModel.passkeys.toList()}")
            }
        },
        onDismiss = {
            deleteDialog = false
        }
    )

    LaunchedEffect(showCopiedToast) {
        if (showCopiedToast) {
            delay(2000)
            showCopiedToast = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .scale(scale)
                    .semantics { role = Role.Button }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        navController.popBackStack()
                    }
            ) {
                Icon(
                    Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .scale(scale)
                    .semantics { role = Role.Button }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        deleteDialog = true
                    }
            ) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp)
        ) {
            // Header Card with Label
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3F51B5)  // TODO(should be dynamic)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 16.dp, bottom = 24.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    Screens.HomeGraph.Passkey.generateRoute(
                                        isEdit = true
                                    )
                                )
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Passkey",
                                tint = Color.White
                            )
                        }
                    }

                    // Label Circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Text(
                            text = passkey.label.firstOrNull()?.uppercase() ?: "",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.W200
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = passkey.service.ifEmpty { "Service" },
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (passkey.desc.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = passkey.desc,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Username Section
                    UsernameField(
                        name = passkey.userName,
                        onCopy = {
                            copyToClipboard(context, "Username", passkey.userName)
                            showCopiedToast = true
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Section
                    PasswordField(
                        key = newKey,
                        isVisible = isKeyVisible,
                        onVisibilityToggle = {
                            isKeyVisible = !isKeyVisible
                        },
                        onValueChange = {
                            newKey = it
                            showSave = it.trim() != passkey.pass
                        }
                    )
                }
            }
            // Action Buttons
            Spacer(modifier = Modifier.height(24.dp))

            if (showSave) {
                SaveChange(
                    onClick = {
                        val newPasskey = passkey.copy(
//                            userName = userName,
//                            TODO( edit password) ??
                        )
//                        viewModel.updatePasskey(newPasskey)
                    }
                )
            }
        }
    }

    // Copied Toast
    AnimatedVisibility(
        visible = showCopiedToast,
        enter = slideInVertically(
            animationSpec = tween(300),
            initialOffsetY = { -it }
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            animationSpec = tween(300),
            targetOffsetY = { -it }
        ) + fadeOut(animationSpec = tween(300)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Copied to clipboard!",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun UsernameField(
    name: String,
    onCopy: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400
            )

            Spacer(modifier = Modifier.weight(1f))

            // Copy Button
            IconButton(
                onClick = onCopy,
                modifier = Modifier
                    .size(32.dp)
            ) {
                Icon(
                    Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun PasswordField(
    key: String,
    isVisible: Boolean,
    onVisibilityToggle: (() -> Unit),
    onValueChange: (String) -> Unit
) {

    var pass by remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                TextField(
                    value = key,
                    onValueChange = {
                        onValueChange(it)
                    },
                    readOnly = !isVisible,
                    visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF8F9FA),
                        unfocusedContainerColor = Color(0xFFF8F9FA),
                        disabledContainerColor = Color(0xFFF8F9FA),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }

            // Visibility Toggle Button
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .semantics { role = Role.Button }
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onVisibilityToggle()
                    }
            ) {
                Icon(
                    if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "Toggle visibility",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(18.dp)
                )
            }
        }
    }
}

@Composable
fun SaveChange(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFAA00FF)
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text("Save Changes")
    }

}

private fun copyToClipboard(context: Context, label: String, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
}

@Composable
fun DeletePasskeyDialog(
    visible: Boolean,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Delete Passkey?") },
            text = { Text("Are you sure you want to delete this passkey?") },
            confirmButton = {
                TextButton(onClick = {
                    onConfirmDelete()
                    onDismiss()
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Preview(showBackground = false)
@Composable
private fun PreviewPassKeyDetail() {
    HashinTheme {
        PasswordField(
            key = "Bolt",
            isVisible = false,
            onVisibilityToggle = {},
            onValueChange = {}
        )
    }
} 