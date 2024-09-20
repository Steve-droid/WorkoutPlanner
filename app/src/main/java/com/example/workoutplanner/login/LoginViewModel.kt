package com.example.workoutplanner.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {
   private val auth = FirebaseAuth.getInstance()
   private val currentUser = auth.currentUser
   private val userCollectionRef = Firebase.firestore.collection("users")
   val uid = currentUser?.uid // This is the user's unique ID


   fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
      auth.signInWithEmailAndPassword(email, password)
         .addOnCompleteListener { task ->
            if (task.isSuccessful) {
               onResult(true, null) // Success
            } else {
               onResult(false, task.exception?.message) // Failure
            }
         }
   }

   fun registerWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
      auth.createUserWithEmailAndPassword(email, password)
         .addOnCompleteListener { task ->
            if (task.isSuccessful) {
               onResult(true, null) // Success
            } else {
               onResult(false, task.exception?.message) // Failure
            }
         }
   }
}