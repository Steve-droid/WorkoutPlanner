package com.example.workoutplanner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.util.UUID

data class User(
   var firstName: String = "",
   var lastName: String = "",
   var email: String = "",
   var userId: String = "",
)


data class ExerciseItem(
   val id: String = UUID.randomUUID().toString(),
   val muscle: String = "",
   val name: String = "",
   val sets: Int = 0,
   val reps: Int = 0,
   val weight: Double = 0.0,
   val instructions: String = ""
)

data class FirestoreExercise(
   val id: String = "",
   val name: String = "",
   val sets: String = "",
   val reps: String = "",
   val weight: Double = 0.0,
   val instructions: String = ""
)

data class Workout(
   val id: String = "",
   var exercises: List<ExerciseItem> = listOf(),
   var day: String = "",
   val name: String = ""
)

data class FirestoreWorkout(
   val id: String = "",
   val day: String = "",
   val name: String = ""
)

data class UIState(
   val currentCycle: FirestoreTrainingCycle = FirestoreTrainingCycle(),
   var workouts: List<WorkoutState> = emptyList(),
   var exerciseListCatalog: MutableState<List<ExerciseItem>> = mutableStateOf(listOf(ExerciseItem())),
   var newWorkoutDay: MutableState<String> = mutableStateOf(""),
   var currentWorkout: MutableState<WorkoutState> = mutableStateOf(WorkoutState()),
   var newWorkout: MutableState<WorkoutState> = mutableStateOf(WorkoutState()),
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
)


data class WorkoutState(
   val id: String = "",
   val day: String = "",
   val name: String = "",
   var exercises: List<ExerciseItem> = listOf(),
)


data class TrainingCycle(
   val cycleName: String = "",
   val numberOfWeeks: Int = 1,
   var workoutList: List<Workout> = listOf()
)

data class FirestoreTrainingCycle(
   val id: String = "",
   val cycleName: String = "",
   val numberOfWeeks: Int = 0,
)