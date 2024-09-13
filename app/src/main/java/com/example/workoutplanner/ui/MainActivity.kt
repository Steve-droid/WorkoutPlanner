package com.example.workoutplanner.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.workoutplanner.ui.theme.WorkoutPlannerTheme

class MainActivity : ComponentActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         WorkoutPlannerTheme {
            Surface(
               modifier = Modifier
                  .fillMaxSize()
                  .background(Color.DarkGray)) {

               WorkoutPlanner()
            }
         }
      }
   }
}











