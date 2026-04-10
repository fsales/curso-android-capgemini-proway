package com.fsales.app.rumo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fsales.app.rumo.ui.navigation.AppNavHost
import com.fsales.app.rumo.ui.theme.RumoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RumoTheme {
                AppNavHost()
            }
        }
    }
}

