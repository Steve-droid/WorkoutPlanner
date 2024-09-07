package com.example.workoutplanner

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private val retrofit =
   Retrofit.Builder()
   .baseUrl("https://api.api-ninjas.com/v1/")
   .addConverterFactory(GsonConverterFactory.create())
   .build()

val exerciseService: ApiService = retrofit.create(ApiService::class.java)

interface ApiService {
   @Headers("X-Api-key: ${BuildConfig.API_KEY}")

   @GET("exercises?muscle")
   suspend fun getExercises(
      @Query("muscle") muscleGroup: String,
      @Query("limit") limit: Int = 10
   ): List<Exercise>
}