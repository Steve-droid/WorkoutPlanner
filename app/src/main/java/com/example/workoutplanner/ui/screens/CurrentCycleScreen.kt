package com.example.workoutplanner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ViewCycleScreen(navController: NavController) {
   Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      Text(text = "This is where you view your current cycle",
      modifier = Modifier.fillMaxWidth(),
      )
   }

}

@Preview(showBackground = true)
@Composable
fun ViewCycleScreenPreview() {
   ViewCycleScreen(navController = rememberNavController())
}