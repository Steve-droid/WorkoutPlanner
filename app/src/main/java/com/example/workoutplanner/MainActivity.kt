package com.example.workoutplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.workoutplanner.ui.WorkoutPlanner
import com.example.workoutplanner.ui.theme.WorkoutPlannerTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
   private val auth = FirebaseAuth.getInstance()
   private val currentUser = auth.currentUser
   private val userCollectionRef = Firebase.firestore.collection("users")
   val uid = currentUser?.uid // This is the user's unique ID


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

                  /* Button(onClick = {
                      val firstName = "Donald"
                      val lastName = "Trump"
                      val user = User(firstName, lastName)
                      saveUser(user)

                   }) {
                   }*/



                  WorkoutPlanner(context = this)
               }

            }
         }
      }
   }


}