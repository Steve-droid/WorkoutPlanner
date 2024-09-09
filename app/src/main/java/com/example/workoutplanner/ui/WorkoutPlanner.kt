package com.example.workoutplanner.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.workoutplanner.model.ExerciseItem
import com.example.workoutplanner.ui.screens.CreateCycleScreen
import com.example.workoutplanner.ui.screens.ExerciseCatalog
import com.example.workoutplanner.ui.screens.HomeScreen
import com.example.workoutplanner.viewmodel.CreateCycleViewModel
import com.example.workoutplanner.viewmodel.WorkoutPlannerViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.serialization.Serializable



@Serializable
data class User(
   val userName: String="",
   val userID:String=""
)

@Serializable
object CreateCycle

@Serializable
object ViewCycle

@Serializable
data class Catalog(val name:String)


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
   model.user = User(userID = "user1", userName = "John Doe")
   val user = model.user

   val database = FirebaseDatabase.getInstance()
   val userReference = database.getReference("users")
   userReference.child(user.userID).setValue(user)

   NavHost(
      navController = navController,
      startDestination = User("Steve", "1")
   ) {
      val createCycleViewModel = CreateCycleViewModel()
      var workoutDay:String = ""


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
         val day = remember {
            mutableStateOf(workoutDay)
         }
         CreateCycleScreen(
            workoutDay = day.value,
             onNavigateToCatalog = {
                navController.navigate(route = Catalog(day.value))
             }
            ,
            onNavigateToHome = {
               navController.navigate(route = User)
            },
            cycleViewModel = createCycleViewModel
         )
      }

      composable<Catalog> {
         ExerciseCatalog(
            onNavigateToNewCycleScreen = {
               navController.navigate(route = CreateCycle)
            },
            createCycleViewModel = createCycleViewModel,
            workoutDay = workoutDay


         )
      }
   }

}

