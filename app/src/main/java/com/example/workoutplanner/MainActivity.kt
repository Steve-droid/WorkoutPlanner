package com.example.workoutplanner

import WorkoutPlannerTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      FirebaseApp.initializeApp(this)


      setContent {
         WorkoutPlannerTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
               Surface(
                  modifier = Modifier.padding(innerPadding)
               ) {
                  WorkoutPlanner()
               }
            }
         }
      }
   }


}