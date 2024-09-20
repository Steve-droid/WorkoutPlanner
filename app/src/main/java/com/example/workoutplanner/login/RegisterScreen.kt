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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.workoutplanner.viewmodel.SharedViewModel

@Composable
fun RegisterScreen(
   sharedViewModel: SharedViewModel,
   onRegisterSuccess: () -> Unit,
   onError: (String) -> Unit
) {
   var email by remember { mutableStateOf("") }
   var firstName by remember { mutableStateOf("") }
   var lastName by remember { mutableStateOf("") }
   var password by remember { mutableStateOf("") }
   var confirmPassword by remember { mutableStateOf("") }
   var isLoading by remember { mutableStateOf(false) }

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      TextField(
         value = firstName,
         onValueChange = { firstName = it },
         label = { Text("Your First Name") },
         singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      TextField(
         value = lastName,
         onValueChange = { lastName = it },
         label = { Text("Your Last Name") },
         singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Email Input Field
      TextField(
         value = email,
         onValueChange = { email = it },
         label = { Text("Email") },
         singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Password Input Field
      TextField(
         value = password,
         onValueChange = { password = it },
         label = { Text("Password") },
         visualTransformation = PasswordVisualTransformation(),
         singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Confirm Password Input Field
      TextField(
         value = confirmPassword,
         onValueChange = { confirmPassword = it },
         label = { Text("Confirm Password") },
         visualTransformation = PasswordVisualTransformation(),
         singleLine = true
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Register Button
      Button(
         onClick = {
            if (password == confirmPassword) {
               isLoading = true
               sharedViewModel.registerWithEmail(
                  email,
                  password,
                  firstName,
                  lastName

               ) { success, errorMessage ->
                  isLoading = false
                  if (success) {
                     onRegisterSuccess()
                  } else {
                     onError(errorMessage ?: "Registration failed")
                  }
               }
            } else {
               onError("Passwords do not match")
            }
         },
         enabled = email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
      ) {
         Text("Register")
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (isLoading) {
         CircularProgressIndicator()
      }
   }
}