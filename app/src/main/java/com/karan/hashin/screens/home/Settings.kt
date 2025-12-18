package com.karan.hashin.screens.home

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.ui.theme.LocalDarkTheme
import com.karan.hashin.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    viewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {}
) {
    val context = LocalContext.current
    val darkThemeState = LocalDarkTheme.current
    var isDarkTheme by darkThemeState
    val coroutineScope = rememberCoroutineScope()

    // User data states
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userBio by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }

    // New states for sheets
    var showNotificationsSheet by remember { mutableStateOf(false) }
    var showSecuritySheet by remember { mutableStateOf(false) }
    var showHelpSheet by remember { mutableStateOf(false) }

    // Delete account states
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }
    var deleteError by remember { mutableStateOf<String?>(null) }

    // Fetch user data from Firestore
    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            try {
                val doc = FirebaseFirestore.getInstance().collection("users").document(user.uid).get().await()
                userName = doc.getString("name") ?: "Unknown"
                userEmail = doc.getString("email") ?: user.email ?: "Unknown"
                userBio = doc.getString("bio") ?: "No bio available"
                userPhone = doc.getString("phone") ?: "No phone number"
            } catch (_: Exception) {
                // Handle error, e.g., show a snackbar or log
                userName = "Error loading data"
                userEmail = "Error loading data"
                userBio = "Error loading data"
                userPhone = "Error loading data"
            }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // Profile Section simplified
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = if (isLoading) "Loading..." else userName,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isLoading) "" else userEmail,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Bio", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = if (isLoading) "" else userBio,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                minLines = 2
                            )
                            Text("Phone", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = if (isLoading) "" else userPhone,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        OutlinedButton(
                            onClick = { showEditSheet = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Edit profile")
                        }
                    }
                }
            }

            item {
                // Preferences Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        ListItem(
                            headlineContent = { Text("Appearance") },
                            supportingContent = { Text("Switch between light and dark mode") },
                            leadingContent = {
                                Icon(Icons.Default.DarkMode, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingContent = {
                                Switch(
                                    checked = isDarkTheme,
                                    onCheckedChange = { newValue ->
                                        isDarkTheme = newValue
                                        setDarkThemePreference(context, newValue)
                                    }
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        ListItem(
                            headlineContent = { Text("Notifications") },
                            supportingContent = { Text("Manage notification preferences") },
                            leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier.clickable { showNotificationsSheet = true },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        ListItem(
                            headlineContent = { Text("Security") },
                            supportingContent = { Text("Manage security settings") },
                            leadingContent = { Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier.clickable { showSecuritySheet = true },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        ListItem(
                            headlineContent = { Text("Help & Support") },
                            supportingContent = { Text("Get help and contact support") },
                            leadingContent = { Icon(Icons.AutoMirrored.Filled.Help, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier.clickable { showHelpSheet = true },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }

            item {
                // Sign Out Button calmer tonal
                OutlinedButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        viewModel.clearLocalData()
                        onSignOut()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign out")
                }
            }

            item {
                // Delete Account Button
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.6f))
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Delete account")
                }
            }
        }
    }

    if (showEditSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var editName by remember { mutableStateOf(userName) }
        var editBio by remember { mutableStateOf(userBio) }
        var editPhone by remember { mutableStateOf(userPhone) }
        var errorText by remember { mutableStateOf<String?>(null) }

        ModalBottomSheet(
            onDismissRequest = { if (!isSaving) showEditSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Edit profile", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = userEmail,
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editBio,
                    onValueChange = { editBio = it },
                    label = { Text("Bio") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = editPhone,
                    onValueChange = { editPhone = it },
                    label = { Text("Phone") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorText != null) {
                    Text(errorText!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { if (!isSaving) showEditSheet = false },
                        modifier = Modifier.weight(1f)
                    ) { Text("Cancel") }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isSaving = true
                                errorText = null
                                val uid = FirebaseAuth.getInstance().currentUser?.uid
                                if (uid == null) {
                                    errorText = "Not signed in"
                                    isSaving = false
                                    return@launch
                                }
                                try {
                                    FirebaseFirestore.getInstance()
                                        .collection("users")
                                        .document(uid)
                                        .update(
                                            mapOf(
                                                "name" to editName,
                                                "bio" to editBio,
                                                "phone" to editPhone
                                            )
                                        ).await()
                                    userName = editName
                                    userBio = editBio
                                    userPhone = editPhone
                                    showEditSheet = false
                                } catch (e: Exception) {
                                    errorText = "Could not save changes"
                                } finally {
                                    isSaving = false
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(if (isSaving) "Saving..." else "Save")
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (showNotificationsSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showNotificationsSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Notifications", style = MaterialTheme.typography.titleLarge)
                Text("Control alerts for vault activity, logins, and updates.")
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Vault activity")
                    Switch(checked = true, onCheckedChange = {})
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("New device login")
                    Switch(checked = true, onCheckedChange = {})
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Product updates")
                    Switch(checked = false, onCheckedChange = {})
                }
                Button(onClick = { showNotificationsSheet = false }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Done")
                }
            }
        }
    }

    if (showSecuritySheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showSecuritySheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Security", style = MaterialTheme.typography.titleLarge)
                Text("Review security tips and sensitive actions.")
                ListItem(
                    headlineContent = { Text("Re-authenticate") },
                    supportingContent = { Text("We may ask you to sign in again before sensitive actions.") },
                    leadingContent = { Icon(Icons.Default.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                )
                ListItem(
                    headlineContent = { Text("Privacy policy") },
                    supportingContent = { Text("Learn how we protect your data.") },
                    leadingContent = { Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                )
                Button(onClick = { showSecuritySheet = false }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Close")
                }
            }
        }
    }

    if (showHelpSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showHelpSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Help & Support", style = MaterialTheme.typography.titleLarge)
                Text("Contact us or browse quick links.")
                ListItem(
                    headlineContent = { Text("Email support") },
                    supportingContent = { Text("support@hashin.app") },
                    leadingContent = { Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                )
                ListItem(
                    headlineContent = { Text("Call us") },
                    supportingContent = { Text("+1 (800) 555-0100") },
                    leadingContent = { Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                )
                ListItem(
                    headlineContent = { Text("Open FAQ") },
                    supportingContent = { Text("Common questions about security and sync.") },
                    leadingContent = { Icon(Icons.Default.SupportAgent, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                )
                Button(onClick = { showHelpSheet = false }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Close")
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { if (!deleting) showDeleteDialog = false },
            icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Delete account?") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("This will remove your vault data from the cloud. This action cannot be undone.")
                    deleteError?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall) }
                }
            },
            confirmButton = {
                TextButton(enabled = !deleting, onClick = {
                    deleting = true
                    viewModel.deleteAccount { success, err ->
                        deleting = false
                        if (success) {
                            showDeleteDialog = false
                            onSignOut()
                        } else {
                            deleteError = err ?: "Could not delete account"
                        }
                    }
                }) {
                    if (deleting) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Delete")
                    }
                }
            },
            dismissButton = {
                TextButton(enabled = !deleting, onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

private fun setDarkThemePreference(context: android.content.Context, isDark: Boolean) {
    val prefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
    prefs.edit().putBoolean("dark_theme", isDark).apply()
}

@Preview
@Composable
private fun PreviewSetting() {
    HashinTheme {
        Settings(viewModel<HomeViewModel>(), rememberNavController())
    }
}

private const val ROUTE_NOTIFICATIONS = "notifications"
private const val ROUTE_SECURITY = "security"
private const val ROUTE_HELP_SUPPORT = "help_support"
