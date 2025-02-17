package com.example.ridehailingapp.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApiService {
    @GET("json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): DirectionsResponse
}