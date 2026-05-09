package com.fsales.app.smartcontact


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fsales.app.smartcontact.ui.navigation.SmartContactNavHost
import com.fsales.app.smartcontact.ui.theme.SmartContactTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartContactTheme {
                SmartContactNavHost()
            }
        }
    }

}