package com.example.demokullu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.demokullu.presentation.navigation.NavGraph
import com.example.demokullu.presentation.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsApp()
        }
    }
}

@Composable
fun NewsApp() {
    var showSplash by remember { mutableStateOf(true) }

    MaterialTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            if (showSplash) {
                SplashScreen {
                    showSplash = false // Navigate to main NavGraph
                }
            } else {
                NavGraph()
            }
        }
    }
}
