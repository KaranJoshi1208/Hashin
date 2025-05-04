package com.karan.hashin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.navigation.NavGraph
import com.karan.hashin.screens.AuthScreen
import com.karan.hashin.ui.theme.HashinTheme
import com.karan.hashin.viewmodel.AuthViewModel
import com.karan.hashin.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private lateinit var provider: ViewModelProvider
    private lateinit var navController: NavHostController
    private lateinit var authViewModel: AuthViewModel
    private lateinit var mainViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        provider = ViewModelProvider(
            this@MainActivity,
            ViewModelProvider.AndroidViewModelFactory()
        )
        authViewModel = provider[AuthViewModel::class.java]
//        mainViewModel = provider[MainViewModel::class.java]

        setContent {
            HashinTheme {
                navController = rememberNavController()
                NavGraph(authViewModel, navController)
            }
        }
    }
}