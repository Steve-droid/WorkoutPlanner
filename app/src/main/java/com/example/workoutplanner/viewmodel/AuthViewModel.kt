package com.example.workoutplanner.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {
   private val auth: FirebaseAuth = FirebaseAuth.getInstance()

   fun signInWithGoogle(idToken: String) {
      val credential = GoogleAuthProvider.getCredential(idToken, null)
      auth.signInWithCredential(credential).addOnCompleteListener { task ->
         if (task.isSuccessful) {
            // User signed in successfully
            val user : FirebaseUser? = auth.currentUser
            // Handle the signed-in user's information here
         } else {
            throw (error("Failed to authenticate. Please try again"))
         }
      }
   }

   fun signOut() {
      auth.signOut()
      // Handle sign-out logic if needed
   }
}
