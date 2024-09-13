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
   val exercise = ExerciseItem(exerciseID = "exercise1",
      name = "Push Up", type = "Strength",
      muscle = "Chest", equipment = "None",
      difficulty = "Beginner", sets = "3",
      reps = "10")




   NavHost(
      navController = navController,
      startDestination = User("Steve", "1")
   ) {
      val createCycleViewModel = CreateCycleViewModel()
      composable<User> { navBackStackEntry ->
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

      composable<CreateCycle> {
         CreateCycleScreen(
            onNavigateToCatalog = { navController.navigate(route = Catalog)},
            vm =createCycleViewModel
         )
      }

      composable<Catalog> {
         ExerciseCatalog(
            onNavigateToNewCycleScreen = {
               navController.navigate(route = CreateCycle)
            },
            vm = createCycleViewModel
         )
      }
   }

}

