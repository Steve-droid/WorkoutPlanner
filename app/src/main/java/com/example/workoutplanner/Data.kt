package com.example.workoutplanner

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Timestamp
import java.util.UUID

data class User(
   var firstName: String = "",
   var lastName: String = "",
   var email: String = "",
   var userId: String = "",
)

data class ExerciseSet(
   val weight: String = "",
   val reps: String = ""
)

data class ExerciseItem(
   var id: String = UUID.randomUUID().toString(),
   val muscle: String = "",
   val name: String = "",
   var sets: List<ExerciseSet> = listOf(),
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


data class FirestoreWorkout(
   val id: String = "",
   val day: String = "",
   val name: String = ""
)

data class UIState(
   val currentCycle: FirestoreTrainingCycle = FirestoreTrainingCycle(),
   var workouts: List<Workout> = emptyList(),
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
)


data class TrainingCycle(
   val id: String = UUID.randomUUID().toString(),
   val cycleName: String = "",
   val numberOfWeeks: Int = 1,
   var workouts: List<Workout> = listOf()
)


data class FirestoreTrainingCycle(
   val id: String = "",
   val cycleName: String = "",
   val numberOfWeeks: Int = 0,
   val createdAt: Timestamp? = null,
   val workoutList: List<Workout> = emptyList()
)

data class Workout(
   var id: String = "",
   val day: String = "",
   val name: String = "",
   var exercises: List<ExerciseItem> = emptyList()
)