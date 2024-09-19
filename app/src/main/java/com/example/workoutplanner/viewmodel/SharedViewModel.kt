package com.example.workoutplanner.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutplanner.ExerciseItem
import com.example.workoutplanner.Workout
import com.example.workoutplanner.exerciseService
import kotlinx.coroutines.launch
import java.util.UUID

data class UIState(
   var workoutList: MutableState<List<Workout>> = mutableStateOf(listOf()),
   var exerciseListCatalog: MutableState<List<ExerciseItem>> = mutableStateOf(listOf(ExerciseItem())),
   var newWorkoutDay: MutableState<String> = mutableStateOf(""),
   var currentWorkout: MutableState<Workout> = mutableStateOf(Workout()),
   var newWorkout: MutableState<Workout> = mutableStateOf(Workout()),
   var currentExercise: MutableState<ExerciseItem> = mutableStateOf(ExerciseItem()),
   var newExercise: MutableState<ExerciseItem> = mutableStateOf(ExerciseItem()),
   var userId: MutableState<String> = mutableStateOf(""),
   var userName: MutableState<String> = mutableStateOf(""),
   var menuExpanded: MutableState<Boolean> = mutableStateOf(false),
   var exerciseSelected: MutableState<Boolean> = mutableStateOf(false),
   var loading: MutableState<Boolean> = mutableStateOf(false),
   var error: MutableState<Boolean> = mutableStateOf(false),
   var searchBar: MutableState<String> = mutableStateOf(""),
   val dayOptions: List<String> = listOf(
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
      "Sunday"
   ),
   var nextWorkoutID: MutableState<Int> = mutableIntStateOf(1)
)

class SharedViewModel : ViewModel() {
   private var state = mutableStateOf(UIState())
   var uiState: MutableState<UIState> = state

   init {

      uiState.value.userId.value = UUID.randomUUID().toString()
      uiState.value.workoutList.value =
         mutableListOf(
            Workout(day = "Sunday", workoutID = 0),
            Workout(day = "Monday", workoutID = 1),
            Workout(day = "Tuesday", workoutID = 2),
            Workout(day = "Wednesday", workoutID = 3),
            Workout(day = "Thursday", workoutID = 4),
            Workout(day = "Friday", workoutID = 5),
            Workout(day = "Saturday", workoutID = 6)
         )

      uiState.value.currentWorkout.value = uiState.value.workoutList.value[0]
      uiState.value.newWorkoutDay = mutableStateOf(uiState.value.currentWorkout.value.day)
   }


   fun fetchExercises() {
      viewModelScope.launch {
         try {
            val newExerciseList = exerciseService.getExercises(uiState.value.searchBar.value)
            uiState.value.exerciseListCatalog.value = newExerciseList

         } catch (e: Exception) {
            uiState.value.error.value = true
            uiState.value.loading.value = false
         }
      }
   }

   fun onClickAddExercise(
      onNavigateToCatalog: () -> Unit,
   ) {
      onNavigateToCatalog()
   }

   fun onSelectExercise() {
      val workoutToAddTo = uiState.value.workoutList.value.find {
         it.day == uiState.value.newWorkoutDay.value
      }

      workoutToAddTo?.exercises?.value =
         workoutToAddTo?.exercises?.value!! + uiState.value.newExercise.value
   }

   fun removeExercise(exercise: ExerciseItem, workout: Workout) {
      workout.exercises.value -= exercise
   }

   fun updateWorkoutDay(workout: Workout, newDay: String) {

      workout.day = newDay
   }
}