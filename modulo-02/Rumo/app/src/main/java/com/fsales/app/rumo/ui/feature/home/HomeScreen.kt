package com.fsales.app.rumo.ui.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.fsales.app.rumo.ui.theme.RumoTheme

@Composable
fun HomeScreen() {
    HomeContent()
}

@Composable
fun HomeContent() {
    RumoTheme {
        val navController = rememberNavController()
        Scaffold() { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)     // respeita insets do Scaffold (top bar + nav bar + teclado)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // permite rolar quando o teclado está visível
            ) {
                Text(text = "Home")
            }
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreview() {
    HomeContent()
}