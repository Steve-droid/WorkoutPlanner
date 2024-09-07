package com.example.workoutplanner.home
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.workoutplanner.view_current_cycle.ViewCycleScreen

@Composable
fun HomeScreen(navController: NavController) {
   Column(
      modifier = Modifier
         .fillMaxSize()
         .padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
   ) {
      Button(
         onClick = { navController.navigate("create_cycle") },
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
      ) {
         Text(text = "Create New Training Cycle")
      }
      Button(
         onClick = { navController.navigate("view_cycle") },
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
      ) {
         Text(text = "View Current Training Cycle")
      }
      Button(
         onClick = { navController.navigate("exercise_catalog") },
         modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
      ) {
         Text(text = "View Exercise Catalog")
      }
   }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
   HomeScreen(navController = rememberNavController())
}