package com.example.workoutplanner.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.workoutplanner.login.LoginScreen
import com.example.workoutplanner.login.LoginViewModel
import com.example.workoutplanner.ui.screens.CreateCycleScreen
import com.example.workoutplanner.ui.screens.ExerciseCatalog
import com.example.workoutplanner.ui.screens.HomeScreen
import com.example.workoutplanner.viewmodel.SharedViewModel


@Composable
fun WorkoutPlanner(
) {
   val navController = rememberNavController()
   val sharedViewModel = SharedViewModel()
   val name = "Dude"
   NavHost(
      navController = navController,
      startDestination = "home_screen/$name"
   ) {

      composable(route = "login_screen") {
         val viewModel = viewModel<LoginViewModel>()
         LoginScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
            onLoggedIn = { userName ->
               navController.navigate(route = "home_screen/$userName") {
               }
            }
         )
      }

      composable(route = "home_screen/{username}") {
         val userName = it.arguments?.getString("username") ?: "no name"
         HomeScreen(
            userName = userName,
            onNavigateToCreateCycle = {
               navController.navigate(route = "create_cycle")
            },
         )
      }

      composable(route = "create_cycle") {
         CreateCycleScreen(
            vm = sharedViewModel,
            onNavigateToCatalog = { day: String ->
               navController.navigate(route = "catalog/$day")
            }
         )
      }

      dialog(route = "catalog/{day}") {
         val day = it.arguments?.getString("day") ?: "no day"
         ExerciseCatalog(
            vm = sharedViewModel,
            onNavigateToNewCycleScreen = {
               navController.popBackStack()
            }
         )
      }
   }
}