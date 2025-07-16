package com.karan.hashin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.karan.hashin.navigation.NavGraph
import com.karan.hashin.ui.theme.HashinTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HashinTheme(dynamicColor = false) {
                navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}