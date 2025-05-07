package com.karan.hashin

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.asPaddingValues
import android.os.Bundle
import android.util.Log
//import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.components.BottomAppBar
import com.karan.hashin.components.TopAppBar
import com.karan.hashin.navigation.NavGraph
import com.karan.hashin.screens.AuthScreen
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.AuthViewModel
import com.karan.hashin.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HashinTheme {
                navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()
                val isAuthenticated = authViewModel.isAuthenticated.collectAsState().value

                Scaffold(
                    topBar = {
                        Log.d("#ined", "isAuthenticated ? $isAuthenticated")
                        if (isAuthenticated) {
                            TopAppBar(Modifier)
                        }
                    },
                    bottomBar = {
                        Log.d("#ined", "isAuthenticated ? $isAuthenticated")
                        if (isAuthenticated) {
                            BottomAppBar(Modifier)
                        }
                    },
                    modifier = Modifier
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) { pd ->
                    NavGraph(pd, navController)
                }
            }
        }
    }
}