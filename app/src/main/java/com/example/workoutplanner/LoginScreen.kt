package com.example.workoutplanner

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp


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
   val context = LocalContext.current

   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(32.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Image(
         modifier = Modifier.size(80.dp),
         painter = painterResource(id = R.drawable.baseline_fitness_center_24),
         contentDescription = "",
         contentScale = ContentScale.FillBounds
      )
      Spacer(modifier = Modifier.height(32.dp))
      Text(
         text = "Sign In To Your Account",
         style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
         ),
         modifier = Modifier.padding(bottom = 16.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))
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
            .padding(bottom = 24.dp)
      )

      Button(
         onClick = {
            isLoading = true
            sharedViewModel.signInWithEmail(
               email = email,
               password = password,
               onSuccess = {
                  isLoading = false
                  onLoginSuccess()
               },
               onError = { errorMessage ->
                  isLoading = false
                  onError(errorMessage)
               }
            )
         },
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
            Text("Sign In")
         }
      }

      Spacer(modifier = Modifier.height(16.dp))

      TextButton(
         onClick = { onRegister() },
         modifier = Modifier.fillMaxWidth()
      ) {
         Text("Don't have an account? Register")
      }

   }
}