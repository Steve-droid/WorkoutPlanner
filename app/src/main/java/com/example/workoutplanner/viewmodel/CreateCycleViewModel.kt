package com.example.workoutplanner.viewmodel

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutplanner.model.ExerciseItem
import com.example.workoutplanner.model.ExerciseState
import com.example.workoutplanner.model.Workout
import com.example.workoutplanner.repository.exerciseService
import com.example.workoutplanner.ui.screens.ExerciseCatalog
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class CreateCycleViewModel : ViewModel() {
   val exerciseState = mutableStateOf(ExerciseState())
   var selectedDay:String = ""
   val dayState = mutableStateOf(selectedDay)
   // Mutable state list of workout days
   var workoutDays = mutableStateOf(
      mutableListOf(
      Workout(day = "Sunday"),
      Workout(day ="Monday"),
      Workout(day ="Tuesday"),
      Workout(day ="Wednesday"),
      Workout(day ="Thursday"),
      Workout(day ="Friday"),
      Workout(day ="Saturday")
   ))
      private set



   fun addExerciseToWorkout(
      userID: String = "",
      cycleID: String="",
      workoutID: String="",
      exercise: ExerciseItem,
      workout: Workout
   ){
      let{
         workout.exercises.value += exercise
      }
      val databaseReference = FirebaseDatabase
         .getInstance()
         .getReference(
            "users/" +
                   "$userID/" +
                   "cycles/" +
                   "$cycleID/" +
                   "workouts/" +
                   "$workoutID/" +
                   "exercises")
      val exerciseID = databaseReference.push().key

      databaseReference.child(exerciseID!!).setValue(exercise).addOnCompleteListener {
         if(it.isCanceled){
          exitProcess(-1)
         }
      }
   }



   fun fetchExercises(exerciseName: String = ""){
      viewModelScope.launch {

         try{
            val exercises = exerciseService.getExercises(exerciseName)
            exerciseState.value = exerciseState.value.copy(
               list = exercises,
               loading = false,
               error = null
            )
         }
         catch (e: Exception){
            exerciseState.value = exerciseState.value.copy(
               loading = false,
               error = "Error while fetching Exercises. Error message: ${e.message}"
            )

         }
      }
   }

   fun addExercise(workout: Workout, exercise: ExerciseItem) {
     let{
        workout.exercises.value += exercise
     }
   }

   fun removeExercise(workout: Workout, exercise: ExerciseItem) {
      let{
         workout.exercises.value -= exercise
      }

   }

   fun updateWorkoutDay(workout: Workout, newDay: String) {
      let{
         workout.day = newDay
      }
   }
}