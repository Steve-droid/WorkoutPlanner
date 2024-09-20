package com.example.workoutplanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController


@Composable
fun WorkoutPlanner() {
   val navController = rememberNavController()
   val sharedViewModel = SharedViewModel()

   NavHost(
      navController = navController,
      startDestination = "login_screen"
   ) {
      composable(route = "login_screen") {
         LoginScreen(
            onLoginSuccess = {
               navController.navigate(route = "home_screen") {
                  popUpTo("login_screen") { inclusive = true }
               }
            },
            onRegister = {
               navController.navigate(route = "register_screen")
            },
            onError = { message ->
               navController.navigate(route = "login_failed/$message")
            },
            sharedViewModel = sharedViewModel
         )
      }

      composable(route = "register_screen") {
         RegisterScreen(
            onRegisterSuccess = {
               navController.navigate(route = "login_screen") {
                  popUpTo("register_screen") { inclusive = true }
               }
            },
            onError = { message ->
               navController.navigate("register_failed/$message")
            },
            sharedViewModel = sharedViewModel
         )
      }

      composable(route = "register_failed/{message}") { backStackEntry ->
         val message = backStackEntry.arguments?.getString("message") ?: ""
         ErrorDialog(
            message = "Failed to register. Error message: $message",
            onDismiss = { navController.navigate("register_screen") }
         )
      }

      composable(route = "login_failed/{message}") { backStackEntry ->
         val message = backStackEntry.arguments?.getString("message") ?: ""
         ErrorDialog(
            message = "Login Failed. Error message: $message",
            onDismiss = { navController.navigate("login_screen") }
         )
      }

      composable(route = "home_screen") {
         HomeScreen(
            onNavigateToCreateCycle = {
               navController.navigate(route = "create_cycle")
            },
            userName = "Steve",
            onNavigateToViewCycles = {
              
            }
         )
      }

      composable(route = "create_cycle") {
         CreateCycleScreen(
            vm = sharedViewModel,
            onNavigateToCatalog = { workoutId ->
               navController.navigate(route = "catalog/$workoutId")
            },
            onNavigateToHome = {
               navController.navigate(route = "home_screen") {
                  popUpTo("home_screen") { inclusive = true }
               }
            }
         )
      }

      dialog(route = "catalog/{workoutId}") { backStackEntry ->
         val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
         ExerciseCatalog(
            vm = sharedViewModel,
            onNavigateToNewCycleScreen = {
               navController.popBackStack()
            },
            workoutId
         )
      }

      composable(route = "view_cycle/{cycleId}") { backStackEntry ->
         val cycleId = backStackEntry.arguments?.getString("cycleId") ?: ""
         ViewCycleScreen(
            sharedViewModel = sharedViewModel,
            cycleId = cycleId,
            onNavigateBack = {
               navController.popBackStack()
            }
         )
      }
   }
}

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit) {
   Dialog(onDismissRequest = onDismiss) {
      Box(
         modifier = Modifier.fillMaxSize(),
         contentAlignment = Alignment.Center
      ) {
         Text(text = message)
      }
   }
}