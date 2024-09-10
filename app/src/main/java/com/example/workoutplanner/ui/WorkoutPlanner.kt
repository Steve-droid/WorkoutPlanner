package com.example.workoutplanner.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.workoutplanner.Catalog
import com.example.workoutplanner.CreateCycle
import com.example.workoutplanner.ExerciseItem
import com.example.workoutplanner.User
import com.example.workoutplanner.ViewCycle
import com.example.workoutplanner.ui.screens.CreateCycleScreen
import com.example.workoutplanner.ui.screens.ExerciseCatalog
import com.example.workoutplanner.ui.screens.HomeScreen
import com.example.workoutplanner.viewmodel.CreateCycleViewModel
import com.example.workoutplanner.viewmodel.WorkoutPlannerViewModel


@Composable
fun WorkoutPlanner(
   model: WorkoutPlannerViewModel = viewModel()
){
   val navController = rememberNavController()

   NavHost(
      navController = navController,
      startDestination = "home_screen"
   ) {
      val createCycleViewModel = CreateCycleViewModel()
      composable("home_screen") { navBackStackEntry ->
         //We now supply a function that returns a composable
         //Define 'home' as a 'Home Screen' object
         val user: User = navBackStackEntry.toRoute()
         // Present the home screen UI by calling a composable function
         HomeScreen(
            user = user,
            onNavigateToCreateCycle = {
               navController.navigate(route = CreateCycle)
            },
            onNavigateToViewCatalog = {
               navController.navigate(route = ViewCycle)
            }
            )
      }

      composable("create_cycle_screen") {
         CreateCycleScreen(
            onNavigateToCatalog = { navController.navigate(route = Catalog)},
            onNavigateToHome = { navController.navigate(route = User)},
            vm =createCycleViewModel
         )
      }

      composable("exercise_catalog_screen") {
         ExerciseCatalog(
            onNavigateToNewCycleScreen = {
               navController.navigate(route = CreateCycle)
            },
            vm = createCycleViewModel
         )
      }
   }

}

