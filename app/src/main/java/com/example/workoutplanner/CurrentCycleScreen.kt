package com.example.workoutplanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ViewCycleScreen(
   sharedViewModel: SharedViewModel,
   cycleId: String,
   onNavigateBack: () -> Unit
) {
   Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      Text(
         text = "This is where you view your current cycle",
         modifier = Modifier.fillMaxWidth(),
      )
   }

}