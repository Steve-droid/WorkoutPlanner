package com.example.workoutplanner.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.workoutplanner.viewmodel.SharedViewModel

@Composable
fun LoginScreen(
   sharedViewModel: SharedViewModel,
   onLoginSuccess: () -> Unit,
   onRegister: () -> Unit,
   onError: (String) -> Unit
) {
   var email by remember { mutableStateOf("") }
   var password by remember { mutableStateOf("") }
   var isLoading by remember { mutableStateOf(false) }
   var isRegistered by remember { mutableStateOf(false) }
   val context = LocalContext.current

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      TextField(
         value = email,
         onValueChange = { email = it },
         label = { Text("Email") }
      )
      Spacer(modifier = Modifier.height(8.dp))
      TextField(
         value = password,
         onValueChange = { password = it },
         label = { Text("Password") },
         visualTransformation = PasswordVisualTransformation()
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(onClick = {
         isLoading = true
         sharedViewModel.signInWithEmail(
            context = context,
            email = email,
            password = password,
            onResult = { success, errorMessage ->
               isLoading = false
               if (success) {
                  onLoginSuccess()
               } else {
                  onError(errorMessage ?: "Unknown error")
               }
            }
         )
      }) {
         Text("Login")
      }

      // Spacer for layout
      Spacer(modifier = Modifier.height(16.dp))

      // Register Button (Navigates to Registration Screen)
      Button(
         onClick = { onRegister() }
      ) {
         Text("Register")
      }

      if (isLoading) {
         CircularProgressIndicator()
      }
   }
}

@Composable
fun GoogleSignInButton(
   onSignInClick: () -> Unit
) {
   Button(onClick = { onSignInClick() }) {
      Text("Sign in with Google")
   }
}