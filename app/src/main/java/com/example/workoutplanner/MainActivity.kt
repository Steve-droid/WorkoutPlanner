package com.example.workoutplanner

import android.os.Bundle
import android.widget.Toast
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

   private val userCollectionRef = Firebase.firestore.collection("users")


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

                  WorkoutPlanner()
               }

            }
         }
      }
   }

   private fun saveUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
      try {
         userCollectionRef.add(user).await()
         withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Successfully Saved Data", Toast.LENGTH_LONG).show()
         }

      } catch (e: Exception) {
         withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
         }
      }
   }
}