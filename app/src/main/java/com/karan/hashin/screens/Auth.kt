package com.karan.hashin.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.karan.hashin.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.karan.hashin.navigation.Screens
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.utils.isValidEmail
import com.karan.hashin.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    navController: NavController,
    innerPadding : PaddingValues,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isNewUser = remember { mutableStateOf(true) }
    val authState = authViewModel.isAuthenticated.collectAsState().value


    if(authState == true) {
        Log.d("#ined", "really isAuthenticated ? ${authViewModel.isAuthenticated.value}")
        navController.navigate(Screens.Home.name)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "#in",
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
            Text(
                text = "Hash Your Secrets",
                fontSize = 28.sp,
                fontWeight = FontWeight.W300,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
            )

            if (isNewUser.value) {
                SignUp(
                    onSuccess = authViewModel::signUp,
                    onFailure = {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    },
                    navigate = {
                        navController.navigate(Screens.Home.name) {
                            popUpTo(Screens.Auth.name) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onClick = {
                        isNewUser.value = false
                    },
                    modifier = Modifier.padding(top = 64.dp)
                )
            } else {
                SignIn(
                    onSuccess = authViewModel::signIn,
                    onFailure = {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    },
                    navigate = {
                        navController.navigate(Screens.Home.name) {
                            popUpTo(Screens.Auth.name) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onClick = {
                        isNewUser.value = true
                    },
                    modifier = Modifier.padding(top = 72.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .height(0.5.dp)
                        .background(color = Color.Black)
                )
                Text("  OR  ", textAlign = TextAlign.Center)
                Spacer(
                    Modifier
                        .weight(1f)
                        .height(0.5.dp)
                        .background(color = Color.Black)
                )
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        val result = authViewModel.googleCredentialAuth(context)
                        result.onSuccess { user ->
                            navController.navigate(Screens.Home.name)
                            Toast.makeText(context, "Google Auth Success !", Toast.LENGTH_SHORT).show()
                        }.onFailure { e ->
                            Log.e("Hashin", "Failed , cannot get the user... 😔", e)
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google",
                    modifier = Modifier
                        .size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continue with Google")
            }
        }
    }
}

@Composable
fun SignIn(
    onSuccess: (String, String, () -> Unit, (Exception) -> Unit) -> Unit,
    onFailure: (Exception) -> Unit,
    navigate : () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passVisible) R.drawable.visibility else R.drawable.visibility_off

                Icon(
                    painter = painterResource(id = image),
                    contentDescription = "Visibility state of password",
                    modifier = Modifier
                        .clickable {
                            passVisible = !passVisible
                        }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if(email.isValidEmail() && pass.isNotBlank()) {
                    onSuccess(
                        email,
                        pass,
                        {
                            navigate()
                            Toast.makeText(context, "Login Success !", Toast.LENGTH_SHORT).show()
                        },
                        {
                            Toast.makeText(context, "Login Failed !", Toast.LENGTH_SHORT).show()
                        }
                    )
                }else {
                    onFailure(Exception("Invalid Details !!!"))
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .width(144.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF673AB7),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 20.dp)
        ) {
            Text("Login")
        }


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {

            Text("New to #in ? ", textAlign = TextAlign.Center, fontSize = 12.sp)
            Text(
                text = "Sign up!",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(true) {
                        onClick()
                    }
            )
        }
    }
}


@Composable
fun SignUp(
    onSuccess: (String, String, String, () -> Unit, (Exception) -> Unit) -> Unit,
    onFailure: (Exception) -> Unit,
    navigate : () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass1 by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }

    var passVisible by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pass1,
            onValueChange = { pass1 = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passVisible) R.drawable.visibility else R.drawable.visibility_off

                Icon(
                    painter = painterResource(id = image),
                    contentDescription = "Visibility state of password",
                    modifier = Modifier
                        .clickable {
                            passVisible = !passVisible
                        }
                )

            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = pass2,
            onValueChange = { pass2 = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (email.isValidEmail() && (pass1 == pass2)) {
                    onSuccess(
                        name,
                        email,
                        pass1,
                        {
                            navigate()
                            Toast.makeText(context, "Sign Up Success !", Toast.LENGTH_SHORT).show()
                        },
                        {
                            Toast.makeText(context, "Sign Up Failed !", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    onFailure(Exception("Incorrect Details"))
                }

            },
            modifier = Modifier
                .padding(top = 16.dp)
                .width(144.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF673AB7),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 20.dp)
        ) {
            Text("Signup")
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {

            Text("Existing User ? ", textAlign = TextAlign.Center, fontSize = 12.sp)
            Text(
                text = "Log in!",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(true) {
                        onClick()
                    }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun AuthPreview() {
    HashinTheme {
//        AuthScreen(viewModel<AuthViewModel>())
    }
}