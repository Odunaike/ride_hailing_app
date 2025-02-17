package com.example.ridehailingapp

import android.annotation.SuppressLint
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
import com.example.ridehailingapp.ui.screens.MapScreen
import com.example.ridehailingapp.ui.theme.RideHailingAppTheme
import com.example.ridehailingapp.ui.viewmodel.MapViewModel
import com.example.ridehailingapp.utils.ManifestUtils
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the API key from the manifest file
        val apiKey = ManifestUtils.getApiKeyFromManifest(this)
        // Initialize the Places API with the retrieved API key
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }

        enableEdgeToEdge()
        setContent {
            RideHailingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mapViewModel = MapViewModel()
                    MapScreen(mapViewModel = mapViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RideHailingAppTheme {
        Greeting("Android")
    }
}