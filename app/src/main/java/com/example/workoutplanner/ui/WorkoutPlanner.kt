package com.example.workoutplanner.ui

import android.content.Context
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
import com.example.workoutplanner.login.LoginScreen
import com.example.workoutplanner.login.RegisterScreen
import com.example.workoutplanner.ui.screens.CreateCycleScreen
import com.example.workoutplanner.ui.screens.ExerciseCatalog
import com.example.workoutplanner.ui.screens.HomeScreen
import com.example.workoutplanner.viewmodel.SharedViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun WorkoutPlanner(
   context: Context
) {
   val navController = rememberNavController()
   val sharedViewModel = SharedViewModel(context)

   val name = "Dude"

   NavHost(
      navController = navController,
      startDestination = "login_screen"
   ) {

      composable(route = "login_screen") {
         LoginScreen(
            onLoginSuccess = {
               navController.navigate(route = "home_screen/$name")
            },
            onRegister = {
               navController.navigate(route = "register_screen")
            },
            onError = {
               navController.navigate(route = "login_failure")
            },
            sharedViewModel = sharedViewModel
         )
      }

      composable(route = "register_screen") {
         RegisterScreen(
            onRegisterSuccess = {
               navController.popBackStack()
            },
            onError = { message ->
               navController.navigate("register_failed/$message")
            },
            sharedViewModel = sharedViewModel
         )
      }

      composable(route = "register_failed/{message}") {
         val message = it.arguments?.getString("message") ?: ""
         Dialog(
            onDismissRequest = { navController.navigate("login_screen") }
         ) {

            Box(
               modifier = Modifier.fillMaxSize(),
               contentAlignment = Alignment.Center
            ) {
               Text(text = "Failed to register. Error message: $message")
            }

         }
      }

      composable(route = "login_failed") {
         Dialog(
            onDismissRequest = { navController.navigate("login_screen") }
         ) {

            Box(modifier = Modifier.fillMaxSize()) {
               Text(text = "Login Failed")
            }

         }
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
            },
            onReturn = {
               navController.popBackStack()
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


fun isUserLoggedIn(): Boolean {
   return FirebaseAuth.getInstance().currentUser != null
}

@Composable
fun AuthCheck(
   onLoggedIn: @Composable () -> Unit,
   onLoggedOut: @Composable () -> Unit
) {
   if (isUserLoggedIn()) {
      onLoggedIn()
   } else {
      onLoggedOut()
   }
}
/*
private fun Auth(user: User) = CoroutineScope(Dispatchers.IO).launch {
   try {
      userCollectionRef.add(user).await()
      withContext(Dispatchers.Main) {
         Toast.makeText(this@MainActivity, "Successfully Saved Data", Toast.LENGTH_LONG).show()
      }

   } catch (e: Exception) {
      withContext(Dispatchers.Main) {
         Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
      }
   }
}*/