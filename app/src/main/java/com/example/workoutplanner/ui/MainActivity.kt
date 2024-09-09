package com.example.workoutplanner.ui

import android.content.Context
import android.credentials.CredentialManager
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
   private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
   val reference : DatabaseReference = database.reference.child("Users")
   var manager = androidx.credentials.CredentialManager.create(this)

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









