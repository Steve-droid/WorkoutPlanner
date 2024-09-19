package com.example.workoutplanner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
   userName: String,
   onNavigateToCreateCycle: () -> Unit,
) {
   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      // Title
      Text(
         text = "Welcome Back, ${userName}!",
         style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
         ),
         modifier = Modifier.padding(bottom = 32.dp)  // Add spacing below the title
      )


      Button(
         onClick = { onNavigateToCreateCycle() },
         modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
         colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
         ),
         shape = MaterialTheme.shapes.medium

      ) {
         Text(
            text = "Create New Training Cycle",
            style = MaterialTheme.typography.bodyLarge.copy(
               fontWeight = FontWeight.Medium
            )
         )
      }
      Button(
         onClick = { },
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
         colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
         ),
         shape = MaterialTheme.shapes.medium
      ) {
         Text(
            text = "View Current Training Cycle",
            style = MaterialTheme.typography.bodyLarge.copy(
               fontWeight = FontWeight.Medium
            )
         )
      }
   }
}