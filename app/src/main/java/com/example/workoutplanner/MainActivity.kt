package com.example.workoutplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.workoutplanner.create_training_cycle.NewCycleScreen
import com.example.workoutplanner.home.HomeScreen
import com.example.workoutplanner.ui.theme.WorkoutPlannerTheme
import com.example.workoutplanner.view_current_cycle.ViewCycleScreen
import com.example.workoutplanner.view_exercise_catalog.ExerciseCatalog
import com.example.workoutplanner.view_exercise_catalog.ExerciseSelection

class MainActivity : ComponentActivity() {
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      enableEdgeToEdge()
      setContent {
         WorkoutPlannerTheme {
          Surface(
             modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)) {
             val navController = rememberNavController()
             AppNavHost(navController)
          }
         }
      }
   }
}

@Composable
fun AppNavHost(navController: NavHostController){
   NavHost(
      navController = navController,
      startDestination = "home"
   ){
      composable("home"){ HomeScreen(navController)}
      composable("create_cycle"){ NewCycleScreen(navController)}
      composable("view_cycle"){ ViewCycleScreen(navController)}
      composable("exercise_catalog"){ ExerciseCatalog(navController)}

   }
}





@Preview(showBackground = true)
@Composable
fun ExerciseSelectionPreview(){
   ExerciseSelection()
}
