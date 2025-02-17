package com.example.ridehailingapp.ui.screens

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.ridehailingapp.data.network.RetrofitClient
import com.example.ridehailingapp.ui.components.SearchBar
import com.example.ridehailingapp.ui.viewmodel.MapViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(mapViewModel: MapViewModel) {
    val cameraPositionState = rememberCameraPositionState()

    val context = LocalContext.current
    // Observe the user's location from the ViewModel
    val userLocation by mapViewModel.userLocation
    // Observe the selected location from the ViewModel
    val selectedLocation by mapViewModel.selectedLocation
    //Observe the distance from the ViewModel
    val distance by mapViewModel.distance
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    //TODO
    val directionsApiKey = "TODO" //enable directions api on

    val tfare by mapViewModel.tfare

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(18.dp)) // Add a spacer with a height of 18dp to push the search bar down

        // Add the search bar component
        SearchBar(
            onPlaceSelected = { place ->
                // When a place is selected from the search bar, update the selected location
                mapViewModel.selectLocation(place, context)
            }
        )

        Box {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                // If the user's location is available, place a marker on the map
                userLocation?.let {
                    Marker(
                        state = MarkerState(position = it), // Place the marker at the user's location
                        title = "Your Location", // Set the title for the marker
                        snippet = "This is where you are currently located." // Set the snippet for the marker
                    )
                    // Move the camera to the user's location with a zoom level of 10f
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 10f)
                }
                // If a location was selected from the search bar, place a marker there
                selectedLocation?.let {
                    Marker(
                        state = MarkerState(position = it), // Place the marker at the selected location
                        title = "Selected Location", // Set the title for the marker
                        snippet = "This is the place you selected." // Set the snippet for the marker
                    )
                    // Move the camera to the selected location with a zoom level of 15f
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
                }

                if (polylinePoints.isNotEmpty()) {
                    Polyline(
                        points = polylinePoints,
                        color = androidx.compose.ui.graphics.Color.Blue,
                        width = 5f
                    )
                }


            }
            Button(
                onClick = {
                    mapViewModel.calculateRideFare()
                    showBottomSheet = true
                },
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            ) {
                Text(text = "Request Ride")
            }

            if (showBottomSheet){
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState,
                        windowInsets = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
                    ) {
                        Text(text = "Ride Fare: $ ${mapViewModel.roundFare(tfare)}")
                    }
                }

            }

        }



        // Check if both locations are available
        if (userLocation != null && selectedLocation != null) {
            // Calculate the distance
            mapViewModel.calculateDistance(userLocation!!, selectedLocation!!)
        }
        //This is called whenever there is a chnage in the userlocation or the selected location
        LaunchedEffect(userLocation, selectedLocation) {
            if (userLocation != null && selectedLocation != null) {
                val origin = "${userLocation!!.latitude},${userLocation!!.longitude}"
                val destination = "${selectedLocation!!.latitude},${selectedLocation!!.longitude}"
                try {
                    val response = RetrofitClient.instance.getDirections(
                        origin = origin,
                        destination = destination,
                        apiKey = directionsApiKey
                    )
                    if (response.routes.isNotEmpty()) {
                        val encodedPolyline = response.routes[0].overview_polyline.points
                        polylinePoints = mapViewModel.decodePolyline(encodedPolyline)
                    }
                } catch (e: Exception) {
                    Log.e("MapScreen", "Error fetching directions: ${e.message}")
                }
            }
        }

        // Handle permission requests for accessing fine location
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Fetch the user's location and update the camera if permission is granted
                mapViewModel.fetchUserLocation(context, fusedLocationClient)
            } else {
                // Handle the case when permission is denied
                Timber.e("Location permission was denied by the user.")
            }
        }

        // Request the location permission when the composable is launched
        LaunchedEffect(Unit) {
            when (PackageManager.PERMISSION_GRANTED) {
                // Check if the location permission is already granted
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    // Fetch the user's location and update the camera
                    mapViewModel.fetchUserLocation(context, fusedLocationClient)
                }

                else -> {
                    // Request the location permission if it has not been granted
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }


    }
}