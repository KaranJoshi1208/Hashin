package com.karan.hashin.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.karan.hashin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
    var name by remember { mutableStateOf("Tara Jain") }
    var email by remember { mutableStateOf("tarajain18@gamil.com") }
    var dobExpanded by remember { mutableStateOf(false) }
    var countryExpanded by remember { mutableStateOf(false) }
    val dobOptions = listOf("13/04/2004")
    val countryOptions = listOf("Indore", "Delhi", "Mumbai", "Bangalore")
    var selectedDob by remember { mutableStateOf(dobOptions[0]) }
    var selectedCountry by remember { mutableStateOf(countryOptions[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Edit Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White,
        bottomBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { /* Save and continue */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2251FF))
                ) {
                    Text("Save And Continue", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            // Profile Image
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Picture",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(90.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Name
            Text(
                text = "Name",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6C6C6C),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2251FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    cursorColor = Color(0xFF2251FF)
                )
            )
            // Email
            Text(
                text = "Email",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6C6C6C),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2251FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    cursorColor = Color(0xFF2251FF)
                )
            )
            // Date of Birth
            Text(
                text = "Date of Birth",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6C6C6C),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            ExposedDropdownMenuBox(
                expanded = dobExpanded,
                onExpandedChange = { dobExpanded = !dobExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedDob,
                    onValueChange = {},
                    readOnly = true,
                    label = null,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dobExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2251FF),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = Color(0xFF2251FF)
                    )
                )
                ExposedDropdownMenu(
                    expanded = dobExpanded,
                    onDismissRequest = { dobExpanded = false }
                ) {
                    dobOptions.forEach { dob ->
                        DropdownMenuItem(
                            text = { Text(dob) },
                            onClick = {
                                selectedDob = dob
                                dobExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Country/Region
            Text(
                text = "Country/Region",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6C6C6C),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            ExposedDropdownMenuBox(
                expanded = countryExpanded,
                onExpandedChange = { countryExpanded = !countryExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCountry,
                    onValueChange = {},
                    readOnly = true,
                    label = null,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2251FF),
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        cursorColor = Color(0xFF2251FF)
                    )
                )
                ExposedDropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false }
                ) {
                    countryOptions.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                selectedCountry = country
                                countryExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen()
}

