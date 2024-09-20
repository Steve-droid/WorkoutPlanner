package com.example.workoutplanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
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
   var errorMessage by remember { mutableStateOf<String?>(null) }

   val scrollState = rememberScrollState()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(32.dp)
         .verticalScroll(scrollState),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Text(
         text = "Create an Account",
         style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
         ),
         modifier = Modifier.padding(bottom = 32.dp)
      )

      OutlinedTextField(
         value = firstName,
         onValueChange = { firstName = it },
         label = { Text("First Name") },
         singleLine = true,
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
      )

      OutlinedTextField(
         value = lastName,
         onValueChange = { lastName = it },
         label = { Text("Last Name") },
         singleLine = true,
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
      )

      OutlinedTextField(
         value = email,
         onValueChange = { email = it },
         label = { Text("Email") },
         singleLine = true,
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
      )

      OutlinedTextField(
         value = password,
         onValueChange = { password = it },
         label = { Text("Password") },
         singleLine = true,
         visualTransformation = PasswordVisualTransformation(),
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
      )

      OutlinedTextField(
         value = confirmPassword,
         onValueChange = { confirmPassword = it },
         label = { Text("Confirm Password") },
         singleLine = true,
         visualTransformation = PasswordVisualTransformation(),
         modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
      )

      Button(
         onClick = {
            if (password == confirmPassword) {
               isLoading = true
               errorMessage = null
               sharedViewModel.registerWithEmail(
                  email,
                  password,
                  firstName,
                  lastName,
                  onError = { error ->
                     isLoading = false
                     errorMessage = error
                  },
                  onSuccess = {
                     isLoading = false
                     onRegisterSuccess()
                  }
               )
            } else {
               errorMessage = "Passwords do not match"
            }
         },
         enabled = !isLoading && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank(),
         modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
      ) {
         if (isLoading) {
            CircularProgressIndicator(
               color = MaterialTheme.colorScheme.onPrimary,
               modifier = Modifier.size(24.dp)
            )
         } else {
            Text("Register")
         }
      }

      errorMessage?.let { error ->
         Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
         )
      }
   }
}